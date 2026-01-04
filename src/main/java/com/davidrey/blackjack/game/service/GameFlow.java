package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.exception.IllegalMoveException;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Hand;
import com.davidrey.blackjack.game.model.HandState;
import com.davidrey.blackjack.game.model.Winner;

public class GameFlow {
    public void validateGameState(GameInfo game) {
        if (game.getGameState() == GameState.FINISHED) {
            throw new IllegalMoveException();
        }
    }

    public boolean allHandsFinished(GameInfo game) {
        return game.getPlayerHands().stream()
                .allMatch(Hand::isFinished);
    }

    public void dealerTurn(GameInfo game) {
        Hand dealer = game.getDealerHand();

        while (dealer.calculateHandValue() < 17) {
            dealer.addCard(game.getDeck().draw());
        }

        dealer.validateBust();
    }

    public void resolveWinners(GameInfo game) {
        int dealerValue = game.getDealerHand().calculateHandValue();
        boolean dealerBust = dealerValue > 21;

        for (Hand hand : game.getPlayerHands()) {
            int playerValue = hand.calculateHandValue();

            if (hand.getState() == HandState.BUST) {
                hand.setWinner(Winner.DEALER);
                continue;
            }

            if (dealerBust) {
                hand.setWinner(Winner.PLAYER);
                continue;
            }

            if (playerValue > dealerValue) {
                hand.setWinner(Winner.PLAYER);
            } else if (playerValue < dealerValue) {
                hand.setWinner(Winner.DEALER);
            } else {
                hand.setWinner(Winner.PUSH);
            }
        }

        game.setGameState(GameState.FINISHED);
    }
}
