package com.davidrey.blackjack.game.dto;

import com.davidrey.blackjack.deck.model.Card;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Winner;

import java.math.BigDecimal;
import java.util.List;

public record GameDto(GameState gameState, List<Card> PlayerHand, List<Card> DealerHand, BigDecimal initialBet, Winner winner) {
}
