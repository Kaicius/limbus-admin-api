package com.kaio.limbus_admin_api.config;

import com.kaio.limbus_admin_api.entity.Sinner;
import com.kaio.limbus_admin_api.repository.SinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SinnerSeeder implements CommandLineRunner {

    private final SinnerRepository sinnerRepository;

    @Override
    public void run(String... args) {
        if (sinnerRepository.count() == 0) {
            List<String> names = List.of(
                    "Yi Sang", "Faust", "Don Quixote", "Ryōshū", "Meursault", "Hong Lu",
                    "Heatcliff", "Ishmael", "Rodion", "Sinclair", "Outis",  "Gregor"
            );
            List<Sinner> sinners = new ArrayList<>();
            for (String name: names) {
                Sinner sinner = new Sinner();
                sinner.setName(name);
                sinnerRepository.save(sinner);
            }
            sinnerRepository.saveAll(sinners);
        }
    }
}
