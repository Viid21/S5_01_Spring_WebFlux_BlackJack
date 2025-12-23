package com.davidrey.blackjack.game.mapper;

import com.davidrey.blackjack.game.document.GameResult;
import com.davidrey.blackjack.game.dto.GameDto;
import reactor.core.publisher.Mono;

public class GameControllerMapper {
    public Mono<GameDto> toMonoDto(Mono<GameResult> game){
        return game.map(gameResult -> new GameDto(
                gameResult.getPlayerCards(),
                gameResult.getDealerCards(),
                gameResult.getInitialBet(),
                gameResult.getWinner()
        ));
    }
}
