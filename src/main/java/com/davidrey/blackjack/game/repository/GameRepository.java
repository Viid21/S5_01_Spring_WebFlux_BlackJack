package com.davidrey.blackjack.game.repository;

import com.davidrey.blackjack.game.document.GameInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public class GameRepository extends ReactiveMongoRepository<GameInfo, UUID> {
}
