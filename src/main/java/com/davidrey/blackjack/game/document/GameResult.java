package com.davidrey.blackjack.game.document;

import com.davidrey.blackjack.deck.model.Card;
import com.davidrey.blackjack.game.model.Winner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
public class GameResult {
    @Id
    private UUID id;
    private UUID playerId;
    List<Card> playerCards;
    List<Card> dealerCards;
    BigDecimal initialBet;
    Winner winner;
}
