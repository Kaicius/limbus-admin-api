package com.kaio.limbus_admin_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record BannerRequest(
        @NotBlank @Size(max = 100) String name,
        String thumbnailUrl,
        Boolean active,
        @NotEmpty Set<Long> identityIds,
        Set<Long> egoIds
) {}
