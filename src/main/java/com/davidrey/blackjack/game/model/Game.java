package com.davidrey.blackjack.game.model;

import com.davidrey.blackjack.deck.Shoe;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Game {
    @Id
    private UUID id = UUID.randomUUID();
    @NonNull
    private UUID playerId;
    @NonNull
    private GameState gameState;
    @NonNull
    private Shoe deck;
    private List<Hand> playerHands = new ArrayList<>();
    private Hand dealerHand = new Hand();
    private BigDecimal insurance;
}
