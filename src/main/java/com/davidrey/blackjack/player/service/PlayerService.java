package com.davidrey.blackjack.player.service;

import com.davidrey.blackjack.exception.PlayerNotFoundException;
import com.davidrey.blackjack.player.dto.PlayerDto;
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

    /*public Mono<Player> newPlayer(String name){
        return Mono<Player>
    }*/

    public Flux<PlayerInfo> getPlayerRanking(){
        return repo.findAll()
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("No one played yet.")))
                .sort(Comparator.comparing(PlayerInfo::getEarnings).reversed());
    }

    public Mono<String> updateName(UUID id, String name){
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with id: " + id)))
                .flatMap(playerInfo -> {
                    playerInfo.setName(name);
                    return repo.save(playerInfo);
                })
                .map(PlayerInfo::getName);
    }
}
