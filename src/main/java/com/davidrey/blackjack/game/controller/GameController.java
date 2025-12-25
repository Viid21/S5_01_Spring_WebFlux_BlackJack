package com.davidrey.blackjack.game.controller;

import com.davidrey.blackjack.game.dto.GameDto;
import com.davidrey.blackjack.game.mapper.GameControllerMapper;
import com.davidrey.blackjack.game.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService service;
    private final GameControllerMapper mapper;

    public GameController(GameService service, GameControllerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<GameDto>> newGame(@RequestBody String playerName) {
        return service.createNewGame(playerName)
                .flatMap(gameResult -> {
                    GameDto dto = mapper.toDto(gameResult);
                    URI uri = URI.create("/game/" + gameResult.getId());
                    return Mono.just(ResponseEntity.created(uri).body(dto));
                });
    }

    @GetMapping("/{id}")
    public Mono<GameDto> getGame(@PathVariable UUID id) {
        return service.getGameById(id)
                .map(mapper::toDto);
    }

    @PostMapping("/{id}/play")
    public void makeMove(@PathVariable UUID id) {

    }

    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable UUID id) {
        service.deleteGameById(id);
        return Mono.just(ResponseEntity.noContent().build());
    }
}
