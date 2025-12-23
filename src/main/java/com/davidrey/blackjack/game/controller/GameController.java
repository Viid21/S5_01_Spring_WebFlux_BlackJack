package com.davidrey.blackjack.game.controller;

import com.davidrey.blackjack.game.dto.GameDto;
import com.davidrey.blackjack.game.mapper.GameControllerMapper;
import com.davidrey.blackjack.game.service.GameService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public void newGame(@RequestBody String name) {

    }

    //Obtenir detalls de la partida
    @GetMapping("/{id}")
    public Mono<GameDto> getGame(@PathVariable UUID id) {
        return mapper.toMonoDto(service.getGameById(id));
    }

    @PostMapping("/{id}/play")
    public void makeMove(@PathVariable UUID id) {

    }

    //Eliminar partida
    public void deleteGame() {

    }


}
