package com.davidrey.blackjack.game.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents the different phases of a blackjack game.")
public enum GameState {

    @Schema(description = "The game is waiting for the initial bet.")
    NEED_INITIAL_BET,

    @Schema(description = "Dealer shows an Ace and the player may place insurance.")
    OFFER_INSURANCE,

    @Schema(description = "The player must choose a move (HIT, STAND, etc.).")
    NEED_PLAYER_MOVE,

    @Schema(description = "The game has ended.")
    FINISHED
}

