package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.exception.GameNotFoundException;
import com.davidrey.blackjack.exception.PlayerNotFoundException;
import com.davidrey.blackjack.game.document.GameResult;
import com.davidrey.blackjack.game.repository.GameRepository;
import com.davidrey.blackjack.player.service.PlayerService;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GameService {
    private final GameRepository repo;
    private final PlayerService playerService;

    public GameService(GameRepository repo, PlayerService playerService) {
        this.repo = repo;
        this.playerService = playerService;
    }

    public void newGame(String playerName) {
        //comprobar si el nom esta guardat a la cache
        //comprobar si el nom esta ala bd de ranking
        //crear un nou player amb akest nom
        //playerService.
    }

    public Mono<GameResult> getGameById(UUID id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)));
    }

    public Mono<ResponseEntity<Void>> deleteGameById(UUID id){
        return repo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new GameNotFoundException(id));
                    }

                    return repo.deleteById(id)
                            .then(Mono.just(ResponseEntity.noContent().build()));
                });
    }
}
