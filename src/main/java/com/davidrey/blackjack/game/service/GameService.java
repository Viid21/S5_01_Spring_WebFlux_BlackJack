package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.game.document.GameResult;
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

    public void newGame(String playerName) {
        //comprobar si el nom esta guardat a la cache
        //comprobar si el nom esta ala bd de ranking
        //crear un nou player amb akest nom
        //playerService.
    }

    public Mono<GameResult> getGameById(UUID id) {
        return repo.findById(id);
    }
}
