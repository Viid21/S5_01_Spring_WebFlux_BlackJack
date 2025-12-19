package com.davidrey.blackjack.player.service;

import com.davidrey.blackjack.exception.PlayerNotFoundException;
import com.davidrey.blackjack.player.entity.PlayerInfo;
import com.davidrey.blackjack.player.repository.PlayerSqlRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PlayerService {
    private final PlayerSqlRepository repo;

    public PlayerService(PlayerSqlRepository repo) {
        this.repo = repo;
    }

    public Mono<String> updateName(UUID id, String name){
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException(id)))
                .flatMap(playerInfo -> {
                    playerInfo.setName(name);
                    return repo.save(playerInfo);
                })
                .map(PlayerInfo::getName);
    }
}
