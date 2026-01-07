package com.davidrey.blackjack.player.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Represents a player in the system.")
public record PlayerDto(

        @Schema(
                description = "Unique identifier of the player.",
                example = "d290f1ee-6c54-4b01-90e6-d701748f0851"
        )
        UUID id,

        @Schema(
                description = "Name of the player.",
                example = "John Doe"
        )
        String name,

        @Schema(
                description = "Total earnings accumulated by the player.",
                example = "150.75"
        )
        BigDecimal earnings
) {}

