package com.davidrey.blackjack.game.dto;

import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Hand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Represents the full state of a blackjack game returned to the client.")
public record GameResponse(

        @Schema(
                description = "Current state of the game (e.g., NEED_PLAYER_MOVE, DEALER_TURN, FINISHED).",
                example = "NEED_PLAYER_MOVE"
        )
        GameState gameState,

        @Schema(
                description = "List of all player hands in the game.",
                implementation = Hand.class
        )
        List<Hand> playerHands,

        @Schema(
                description = "The dealer's hand.",
                implementation = Hand.class
        )
        Hand dealerHand
) {}

