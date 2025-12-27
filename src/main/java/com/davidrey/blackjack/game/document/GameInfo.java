package com.davidrey.blackjack.game.document;

import com.davidrey.blackjack.deck.Shoe;
import com.davidrey.blackjack.deck.model.Card;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Winner;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "games")
public class GameInfo {
    @Id
    private UUID id = UUID.randomUUID();
    @NonNull
    private UUID playerId;
    @NonNull
    private GameState gameState;
    @NonNull
    private Shoe deck;
    private List<Card> playerCards;
    private List<Card> dealerCards;
    private BigDecimal initialBet;
    private Winner winner;
}
