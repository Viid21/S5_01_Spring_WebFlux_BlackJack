package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.PlayRequest;

public class GameLogic {
    private final PlayerActions actions;
    private final GameFlow flow;

    public GameLogic(PlayerActions actions, GameFlow flow) {
        this.actions = actions;
        this.flow = flow;
    }

    public GameInfo play(GameInfo gameInfo, PlayRequest request) {
        flow.validateGameState(gameInfo);

        actions.apply(gameInfo, request);

        if (flow.allHandsFinished(gameInfo)) {
            flow.dealerTurn(gameInfo);
            flow.resolveWinners(gameInfo);
        }

        return gameInfo;
    }

}
