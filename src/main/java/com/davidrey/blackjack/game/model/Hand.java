package com.davidrey.blackjack.game.model;

import com.davidrey.blackjack.deck.model.Card;
import com.davidrey.blackjack.deck.model.Rank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Represents a single blackjack hand.")
public class Hand {

    @Schema(
            description = "List of cards in the hand.",
            implementation = Card.class
    )
    private List<Card> cards = new ArrayList<>();

    @Schema(
            description = "Current state of the hand.",
            implementation = HandState.class
    )
    private HandState state = HandState.ACTIVE;

    @Schema(
            description = "Bet amount associated with this hand.",
            example = "20.00"
    )
    private BigDecimal bet = BigDecimal.ZERO;

    @Schema(
            description = "Winner of this hand after the game ends.",
            implementation = Winner.class
    )
    private Winner winner;

    public void addCard(Card card) {
        cards.add(card);
        validateBust();
    }

    public int calculateHandValue() {
        int sum = 0;
        int aces = 0;

        for (Card card : this.cards) {
            int value = card.rank().getValue();

            if (card.rank() == Rank.ACE) {
                aces++;
                value = 11;
            }

            sum += value;
        }

        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }

        return sum;
    }

    public void validateBust() {
        if (calculateHandValue() > 21) {
            this.state = HandState.BUST;
        }
    }
    public void validateNatBj() {
        if (cards.size() == 2 && calculateHandValue() == 21) {
            this.state = HandState.NAT_BJ;
        }
    }

    public boolean isFinished() {
        return state.isFinished();
    }


}
