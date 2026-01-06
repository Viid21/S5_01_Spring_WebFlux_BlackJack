package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.game.model.Game;
import com.davidrey.blackjack.game.model.Hand;
import com.davidrey.blackjack.game.model.HandState;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class GameCrupier {
    public BigDecimal calculateEarnings(Game game) {
        BigDecimal total = BigDecimal.ZERO;

        for (Hand hand : game.getPlayerHands()) {
            BigDecimal bet = hand.getBet();
            BigDecimal result = BigDecimal.ZERO;

            switch (hand.getWinner()) {
                case PLAYER -> {
                    if (hand.getState() == HandState.NAT_BJ) {
                        result = bet.multiply(BigDecimal.valueOf(1.5));
                    } else {
                        result = bet;
                    }
                }
                case DEALER -> result = bet.negate();
                case PUSH -> result = BigDecimal.ZERO;
                case SURRENDER -> result = bet.divide(BigDecimal.valueOf(2), RoundingMode.DOWN).negate();
            }

            total = total.add(result);
        }

        if (game.getInsurance() != null) {
            game.getDealerHand().validateNatBj();
            boolean dealerBJ = game.getDealerHand().getState() == HandState.NAT_BJ;
            if (dealerBJ) {
                total = total.add(game.getInsurance().multiply(BigDecimal.valueOf(2)));
            } else {
                total = total.subtract(game.getInsurance());
            }
        }

        return total;
    }
}
