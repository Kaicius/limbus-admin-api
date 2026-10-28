package com.kaio.limbus_admin_api.dto;

import com.kaio.limbus_admin_api.enums.Rarity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IdentityRequest (
        @NotBlank
        @Size(max = 100) String name,
        @NotNull
        Rarity rarity,
        @NotNull Long sinnerId,
        String uptie1ArtUrl,
        String uptie3ArtUrl,
        String uptieCutscene,
        String obtainmentVideoUrl
) {}
