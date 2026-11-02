package com.kaio.limbus_admin_api.config;

import com.kaio.limbus_admin_api.entity.Identity;
import com.kaio.limbus_admin_api.entity.Sinner;
import com.kaio.limbus_admin_api.enums.Rarity;
import com.kaio.limbus_admin_api.repository.IdentityRepository;
import com.kaio.limbus_admin_api.repository.SinnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.text.Normalizer;
import java.util.Map;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class LcbIdentitySeeder implements CommandLineRunner {

    private final SinnerRepository sinnerRepository;
    private final IdentityRepository identityRepository;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.upload-preset}")
    private String uploadPreset;

    private final RestClient restClient = RestClient.create();

    @Override
    public void run(String... args) {
        for (Sinner sinner : sinnerRepository.findAll()) {
            if (identityRepository.existsBySinnerIdAndRarity(sinner.getId(), Rarity.ZERO)) {
                continue;
            }

            ClassPathResource image = new ClassPathResource("seed/lcb/" + slug(sinner.getName()) + ".jpg");
            if (!image.exists()) {
                log.warn("Seed LCB: imagem {} não encontrada, pulando {}", image.getPath(), sinner.getName());
                continue;
            }

            try {
                String url = uploadToCloudinary(image);

                Identity identity = new Identity();
                identity.setName("LCB Sinner " + sinner.getName());
                identity.setRarity(Rarity.ZERO);
                identity.setSinner(sinner);
                identity.setUptie1ArtUrl(url);
                identityRepository.save(identity);

                log.info("Seed LCB: criada {}", identity.getName());
            } catch (Exception e) {
                log.warn("Seed LCB: falha no upload de {} ({}). Tenta de novo na próxima execução.",
                        sinner.getName(), e.getMessage());
            }
        }
    }

    private String uploadToCloudinary(ClassPathResource image) {
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", image);
        form.add("upload_preset", uploadPreset);

        Map<?, ?> response = restClient.post()
                .uri("https://api.cloudinary.com/v1_1/{cloud}/image/upload", cloudName)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(form)
                .retrieve()
                .body(Map.class);

        if (response == null || response.get("secure_url") == null) {
            throw new IllegalStateException("Cloudinary não devolveu secure_url");
        }
        return (String) response.get("secure_url");
    }

    private static String slug(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim()
                .replace(' ', '-');
    }
}
