package com.davidrey.blackjack.player.repository;

import com.davidrey.blackjack.player.entity.PlayerInfo;
import lombok.NonNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface PlayerSqlRepository extends ReactiveCrudRepository<@NonNull PlayerInfo, @NonNull UUID> {
}
