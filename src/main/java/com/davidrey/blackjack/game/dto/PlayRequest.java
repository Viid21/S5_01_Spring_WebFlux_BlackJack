package com.davidrey.blackjack.game.dto;

import com.davidrey.blackjack.game.model.PlayerMove;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record PlayRequest(
        @Schema(description = "Player move", example = "HIT")
        PlayerMove move,

        @Schema(description = "Index of the hand to apply the move to", example = "0")
        Integer handIndex,

        @Schema(description = "Amount wagered (only for INITIAL_BET, DOUBLE, INSURANCE)", example = "10")
        BigDecimal amount
) {
}
