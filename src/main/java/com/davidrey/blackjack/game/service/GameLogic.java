package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.deck.model.Card;
import com.davidrey.blackjack.deck.model.Rank;
import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.exception.IllegalBetException;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Winner;

import java.math.BigDecimal;
import java.util.List;

public class GameLogic {
    public GameInfo playerActions(GameInfo gameInfo, PlayRequest request) {
        return switch (request.move()) {
            case INITIAL_BET -> setInitialBet(gameInfo, request.amount());
            case HIT -> hit(gameInfo);
            case STAY -> stay(gameInfo, request);
            case SPLIT -> split(gameInfo, request);
            case DOBLE_DOWN -> dobleDown(gameInfo, request);
            case INSURANCE -> insurance(gameInfo, request);
        };
    }

    public GameInfo setInitialBet(GameInfo gameInfo, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalBetException();
        }

        gameInfo.setInitialBet(amount);

        gameInfo.getDeck().shuffle();
        gameInfo.getPlayerCards().add(gameInfo.getDeck().draw());
        gameInfo.getDealerCards().add(gameInfo.getDeck().draw());
        gameInfo.getPlayerCards().add(gameInfo.getDeck().draw());

        gameInfo.setGameState(GameState.NEED_PLAYER_MOVE);
        return gameInfo;
    }

    public GameInfo hit(GameInfo gameInfo) {
        gameInfo.getPlayerCards().add(gameInfo.getDeck().draw());
        return validatePlayerCardPoints(gameInfo);
    }

    public GameInfo stay(GameInfo gameInfo, PlayRequest request) {
        gameInfo.setGameState(GameState.DEALER_TURN);
        return gameInfo;
    }

    public GameInfo split(GameInfo gameInfo, PlayRequest request) {
        //mirar kin tipus de jugada vol fer el jugador
        //mirar k la jugada es pugui realitzar
        //realitzarla
        //retornar la info amb la jugada realitzada
        return null;
    }

    public GameInfo dobleDown(GameInfo gameInfo, PlayRequest request) {
        //mirar kin tipus de jugada vol fer el jugador
        //mirar k la jugada es pugui realitzar
        //realitzarla
        //retornar la info amb la jugada realitzada
        return null;
    }

    public GameInfo insurance(GameInfo gameInfo, PlayRequest request){
        return null;
    }

    public GameInfo validatePlayerCardPoints(GameInfo gameInfo) {
        if (calculateHandValue(gameInfo.getDealerCards()) > 21) {
            return finishGame(gameInfo, Winner.DEALER);
        }
        return gameInfo;
    }

    public int calculateHandValue(List<Card> cards) {
        int sum = 0;
        int aces = 0;

        for (Card card : cards) {
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

    public GameInfo finishGame(GameInfo gameInfo, Winner winner) {
        gameInfo.setGameState(GameState.FINISHED);
        gameInfo.setWinner(winner);
        return gameInfo;
    }
}
