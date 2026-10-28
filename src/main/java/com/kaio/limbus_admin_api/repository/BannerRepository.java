package com.kaio.limbus_admin_api.repository;

import com.kaio.limbus_admin_api.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    long count();
}
