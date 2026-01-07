package com.davidrey.blackjack.game.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Possible moves a player can make during a blackjack game.")
public enum PlayerMove {

    @Schema(description = "Place the initial bet.")
    INITIAL_BET,

    @Schema(description = "Draw an additional card.")
    HIT,

    @Schema(description = "Stop drawing cards.")
    STAND,

    @Schema(description = "Double the bet and draw exactly one more card.")
    DOBLE_DOWN,

    @Schema(description = "Split the hand into two separate hands.")
    SPLIT,

    @Schema(description = "Place an insurance bet when the dealer shows an Ace.")
    INSURANCE,

    @Schema(description = "Surrender the hand and lose half the bet.")
    SURRENDER

}
