package com.davidrey.blackjack.player.service;

import com.davidrey.blackjack.exception.PlayerNotFoundException;
import com.davidrey.blackjack.player.entity.PlayerInfo;
import com.davidrey.blackjack.player.repository.PlayerSqlRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.UUID;

@Service
public class PlayerService {
    private final PlayerSqlRepository repo;

    public PlayerService(PlayerSqlRepository repo) {
        this.repo = repo;
    }

    public Mono<PlayerInfo> getPlayerForNewGame(String name) {
        return repo.findByName(name)
                .switchIfEmpty(repo.save(new PlayerInfo(name)));
    }

    public Flux<PlayerInfo> getPlayerRanking() {
        return repo.findAll()
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("No one played yet.")))
                .sort(Comparator.comparing(PlayerInfo::getEarnings).reversed());
    }

    public Mono<PlayerInfo> updateName(UUID id, String newName) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with id: " + id)))
                .flatMap(playerInfo -> {
                    playerInfo.setName(newName);
                    return repo.save(playerInfo);
                });
    }
}
