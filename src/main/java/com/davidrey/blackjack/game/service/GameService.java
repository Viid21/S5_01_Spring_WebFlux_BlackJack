package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.deck.Shoe;
import com.davidrey.blackjack.game.exception.GameNotFoundException;
import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.mapper.GameServiceMapper;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.repository.GameRepository;
import com.davidrey.blackjack.player.service.PlayerService;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GameService {
    private final GameRepository repo;
    private final PlayerService playerService;
    private final GameLogic logic;
    private final GameCrupier crupier;
    private final GameServiceMapper mapper;

    public GameService(GameRepository repo, PlayerService playerService, GameLogic logic, GameCrupier crupier, GameServiceMapper mapper) {
        this.repo = repo;
        this.playerService = playerService;
        this.logic = logic;
        this.crupier = crupier;
        this.mapper = mapper;
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
                .map(gameInfo -> logic.play(mapper.toGame(gameInfo), request))
                .flatMap(game -> {
                    if (game.getGameState() == GameState.FINISHED) {
                        return playerService.updatePlayerEarnings(game.getPlayerId(), crupier.calculateEarnings(game))
                                .then(repo.save(mapper.toInfo(game)));
                    } else {
                        return repo.save(mapper.toInfo(game));
                    }
                });
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