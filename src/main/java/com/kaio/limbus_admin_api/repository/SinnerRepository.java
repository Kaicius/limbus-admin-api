package com.kaio.limbus_admin_api.repository;

import com.kaio.limbus_admin_api.entity.Sinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinnerRepository extends JpaRepository<Sinner, Long> {
    long count();
}