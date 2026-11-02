package com.kaio.limbus_admin_api.service;

import com.kaio.limbus_admin_api.dto.BannerRequest;
import com.kaio.limbus_admin_api.dto.BannerResponse;
import com.kaio.limbus_admin_api.entity.Banner;
import com.kaio.limbus_admin_api.entity.Ego;
import com.kaio.limbus_admin_api.entity.Identity;
import com.kaio.limbus_admin_api.enums.Rarity;
import com.kaio.limbus_admin_api.expection.ResourceNotFoundException;
import com.kaio.limbus_admin_api.repository.BannerRepository;
import com.kaio.limbus_admin_api.repository.EgoRepository;
import com.kaio.limbus_admin_api.repository.IdentityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

    private final BannerRepository bannerRepository;
    private final IdentityRepository identityRepository;
    private final EgoRepository egoRepository;

    public BannerResponse create(BannerRequest req) {
        Banner banner = new Banner();
        applyRequest(banner, req);
        return BannerResponse.from(bannerRepository.save(banner));
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> all() {
        return bannerRepository.findAll().stream()
                .map(BannerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BannerResponse findId(Long id) {
        return BannerResponse.from(findBanner(id));
    }

    public BannerResponse update(Long id, BannerRequest req) {
        Banner banner = findBanner(id);
        applyRequest(banner, req);
        return BannerResponse.from(bannerRepository.save(banner));
    }

    public void delete(Long id) {
        bannerRepository.delete(findBanner(id));
    }


    private Banner findBanner(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner " + id + " não encontrado"));
    }

    private void applyRequest(Banner banner, BannerRequest req) {
        Set<Identity> identities = loadIdentities(req.identityIds());
        validateRarities(identities);

        banner.setName(req.name());
        banner.setThumbnailUrl(req.thumbnailUrl());
        banner.setActive(req.active() == null || req.active());
        banner.setIdentities(identities);
        banner.setEgos(loadEgos(req.egoIds()));
    }

    private Set<Identity> loadIdentities(Set<Long> ids) {
        List<Identity> found = identityRepository.findAllById(ids);
        if (found.size() != ids.size()) {
            throw new ResourceNotFoundException("Alguma identity não existe: " + ids);
        }
        return new HashSet<>(found);
    }

    private Set<Ego> loadEgos(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        List<Ego> found = egoRepository.findAllById(ids);
        if (found.size() != ids.size()) {
            throw new ResourceNotFoundException("Algum EGO não existe: " + ids);
        }
        return new HashSet<>(found);
    }

    private void validateRarities(Set<Identity> identities) {
        Set<Rarity> present = identities.stream()
                .map(Identity::getRarity)
                .collect(Collectors.toSet());

        if (!present.containsAll(EnumSet.allOf(Rarity.class))) {
            throw new IllegalArgumentException(
                    "O banner precisa ter pelo menos uma identity de cada raridade (0, 00 e 000)");
        }
    }
}