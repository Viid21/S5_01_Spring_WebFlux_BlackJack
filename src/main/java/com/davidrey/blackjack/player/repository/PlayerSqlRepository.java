package com.davidrey.blackjack.player.repository;

import com.davidrey.blackjack.player.entity.PlayerInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerSqlRepository extends ReactiveCrudRepository<PlayerInfo, UUID> {
    Mono<PlayerInfo> findByName(String name);
}
