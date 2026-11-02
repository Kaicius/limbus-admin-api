package com.kaio.limbus_admin_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EgoRequest (
        @NotBlank
        @Size(max = 100) String name,
        @NotNull Long sinnerId,
        String artUrl,
        String obtainmentVideoUrl
) {}