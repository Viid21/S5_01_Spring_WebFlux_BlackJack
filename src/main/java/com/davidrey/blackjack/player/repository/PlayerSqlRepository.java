package com.davidrey.blackjack.player.repository;

import com.davidrey.blackjack.player.entity.PlayerInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PlayerSqlRepository extends ReactiveCrudRepository<PlayerInfo, UUID> {
    Mono<PlayerInfo> findByName(String name);
}
