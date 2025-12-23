package com.davidrey.blackjack.player.controller;

import com.davidrey.blackjack.player.dto.PlayerDto;
import com.davidrey.blackjack.player.mapper.PlayerControllerMapper;
import com.davidrey.blackjack.player.service.PlayerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class PlayerController {
    private final PlayerService service;
    private final PlayerControllerMapper mapper;

    public PlayerController(PlayerService service, PlayerControllerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/ranking")
    public Flux<PlayerDto> getRanking() {
        return mapper.toFluxDto(service.getPlayerRanking());
    }

    @PutMapping("/player/{id}")
    public Mono<String> updatePlayerName(@PathVariable UUID id, @RequestBody String name){
        return service.updateName(id,name);
    }
}
