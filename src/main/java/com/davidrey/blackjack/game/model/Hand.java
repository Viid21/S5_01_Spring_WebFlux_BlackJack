package com.davidrey.blackjack.game.model;

import com.davidrey.blackjack.deck.model.Card;
import com.davidrey.blackjack.deck.model.Rank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Hand {
    private List<Card> cards = new ArrayList<>();
    private HandState state = HandState.ACTIVE;
    private BigDecimal bet = BigDecimal.ZERO;
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
