package com.davidrey.blackjack.player.repository;

import com.davidrey.blackjack.player.document.PlayerStats;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface PlayerMongoRepository extends ReactiveMongoRepository<@NonNull PlayerStats, @NonNull UUID> {
}
