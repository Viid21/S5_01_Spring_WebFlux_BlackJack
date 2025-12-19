package com.davidrey.blackjack.player.controller;

import com.davidrey.blackjack.player.service.PlayerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class PlayerController {
    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @PutMapping("/player/{id}")
    public Mono<String> updatePlayerName(@PathVariable UUID id, @RequestBody String name){
        return service.updateName(id,name);
    }
}
