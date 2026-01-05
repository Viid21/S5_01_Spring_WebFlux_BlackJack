package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.deck.model.Rank;
import com.davidrey.blackjack.game.exception.IllegalBetException;
import com.davidrey.blackjack.game.exception.IllegalMoveException;
import com.davidrey.blackjack.game.model.Hand;
import com.davidrey.blackjack.game.model.HandState;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HandRules {
    public void validateBasicBet(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalBetException();
        }
    }

    public void validateHandPlayable(Hand hand) {
        hand.validateBust();
        if (hand.getState().isFinished()) {
            throw new IllegalMoveException();
        }
    }

    public void validateSplit(Hand hand, BigDecimal splitAmount, BigDecimal originalBet) {
        validateBasicBet(splitAmount);

        if (splitAmount.compareTo(originalBet) > 0) {
            throw new IllegalBetException();
        }

        if (hand.getCards().size() != 2 ||
                !hand.getCards().getFirst().rank().equals(hand.getCards().getLast().rank())) {
            throw new IllegalMoveException();
        }
    }

    public void validateDoubleDown(Hand hand, BigDecimal extraAmount, BigDecimal originalBet) {
        validateBasicBet(extraAmount);

        if (extraAmount.compareTo(originalBet) > 0) {
            throw new IllegalBetException();
        }

        if (hand.getCards().size() > 2) {
            throw new IllegalMoveException();
        }
    }

    public void validateInsurance(BigDecimal originalBet, BigDecimal insuranceAmount) {
        validateBasicBet(insuranceAmount);
        BigDecimal halfBet = originalBet
                .divide(BigDecimal.valueOf(2), RoundingMode.DOWN);

        if (insuranceAmount.compareTo(halfBet) > 0) {
            throw new IllegalBetException();
        }
    }

    public HandState splitAceVerification(Hand hand) {
        if (hand.getCards().getFirst().rank().equals(Rank.ACE)) {
            return HandState.STAND;
        }
        return HandState.ACTIVE;
    }
}
