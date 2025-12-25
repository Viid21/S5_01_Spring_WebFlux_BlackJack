package com.davidrey.blackjack.game.mapper;

import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.GameDto;

public class GameControllerMapper {
    public GameDto toDto(GameInfo gameInfo) {
        return new GameDto(
                gameInfo.getGameState(),
                gameInfo.getPlayerCards(),
                gameInfo.getDealerCards(),
                gameInfo.getInitialBet(),
                gameInfo.getWinner()
        );
    }
}
