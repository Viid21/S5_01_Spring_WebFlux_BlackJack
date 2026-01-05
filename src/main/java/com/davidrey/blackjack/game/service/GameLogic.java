package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.game.model.Game;
import com.davidrey.blackjack.game.dto.PlayRequest;

public class GameLogic {
    private final PlayerActions actions;
    private final GameFlow flow;

    public GameLogic(PlayerActions actions, GameFlow flow) {
        this.actions = actions;
        this.flow = flow;
    }

    public Game play(Game game, PlayRequest request) {
        flow.validateGameState(game);

        actions.apply(game, request);

        if (flow.allHandsFinished(game)) {
            flow.dealerTurn(game);
            flow.resolveWinners(game);
        }

        return game;
    }

}
