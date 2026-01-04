package com.davidrey.blackjack.game.mapper;

import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.GameResponse;

public class GameControllerMapper {
    public GameResponse toDto(GameInfo gameInfo) {
        return new GameResponse(
                gameInfo.getGameState(),
                gameInfo.getPlayerHands(),
                gameInfo.getDealerHand()
        );
    }
}
