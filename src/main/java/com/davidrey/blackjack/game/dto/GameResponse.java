package com.davidrey.blackjack.game.dto;

import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Hand;

import java.util.List;

public record GameResponse(
        GameState gameState,
        List<Hand> PlayerHands,
        Hand DealerHand) {
}
