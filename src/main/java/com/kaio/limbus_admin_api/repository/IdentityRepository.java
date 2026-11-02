package com.kaio.limbus_admin_api.repository;

import com.kaio.limbus_admin_api.entity.Identity;
import com.kaio.limbus_admin_api.enums.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityRepository extends JpaRepository<Identity, Long> {
    long count();

    boolean existsBySinnerIdAndRarity(Long sinnerId, Rarity rarity);
}
