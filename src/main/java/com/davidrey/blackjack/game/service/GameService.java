package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.exception.GameNotFoundException;
import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.repository.GameRepository;
import com.davidrey.blackjack.player.service.PlayerService;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GameService {
    private final GameRepository repo;
    private final PlayerService playerService;

    public GameService(GameRepository repo, PlayerService playerService) {
        this.repo = repo;
        this.playerService = playerService;
    }

    public Mono<GameInfo> createNewGame(String playerName) {
        return playerService.getPlayerForNewGame(playerName)
                .flatMap(playerInfo -> {
                    GameInfo gameInfo = new GameInfo(playerInfo.getId());
                    gameInfo.setGameState(GameState.NEED_INITIAL_BET);
                    return repo.save(gameInfo);
                });
    }

    //comprobar k la initial bet es pot realitzar y no te saldo insuficient

    //començar la partida

    // aquí faries la lògica de començar la partida
    // game.play(), etc.

    public Mono<GameInfo> getGameById(UUID id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)));
    }

    public Mono<Void> deleteGameById(UUID id) {
        return repo.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new GameNotFoundException(id));
            }
            return repo.deleteById(id);
        });
    }
}
