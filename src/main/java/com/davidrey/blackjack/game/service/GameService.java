package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.deck.Shoe;
import com.davidrey.blackjack.game.exception.GameAlreadyEndedException;
import com.davidrey.blackjack.game.exception.GameNotFoundException;
import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.repository.GameRepository;
import com.davidrey.blackjack.player.service.PlayerService;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GameService {
    private final GameRepository repo;
    private final PlayerService playerService;
    private final GameLogic logic;

    public GameService(GameRepository repo, PlayerService playerService, GameLogic logic) {
        this.repo = repo;
        this.playerService = playerService;
        this.logic = logic;
    }

    public Mono<GameInfo> createNewGame(String playerName) {
        return playerService.getPlayerForNewGame(playerName)
                .flatMap(playerInfo -> {
                    GameInfo gameInfo = new GameInfo(
                            playerInfo.getId(),
                            GameState.NEED_INITIAL_BET,
                            new Shoe(6));

                    return repo.save(gameInfo);
                });
    }

    public Mono<GameInfo> play(UUID id, PlayRequest request) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)))
                .filter(gameInfo -> gameInfo.getWinner() == null)
                .switchIfEmpty(Mono.error(new GameAlreadyEndedException(id)))
                .map(gameInfo -> logic.playerActions(gameInfo, request));
    }

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
