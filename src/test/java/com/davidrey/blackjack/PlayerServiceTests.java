package com.davidrey.blackjack;

import com.davidrey.blackjack.player.entity.PlayerInfo;
import com.davidrey.blackjack.player.exception.EmptyRankingException;
import com.davidrey.blackjack.player.exception.PlayerNotFoundException;
import com.davidrey.blackjack.player.repository.PlayerSqlRepository;
import com.davidrey.blackjack.player.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PlayerServiceTests {

    private PlayerSqlRepository repo;
    private PlayerService service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(PlayerSqlRepository.class);
        service = new PlayerService(repo);
    }

    @Test
    void getPlayerForNewGame_returnsExistingPlayer() {
        PlayerInfo existing = new PlayerInfo("Alice");
        when(repo.findByName("Alice")).thenReturn(Mono.just(existing));

        StepVerifier.create(service.getPlayerForNewGame("Alice"))
                .expectNext(existing)
                .verifyComplete();
    }

    @Test
    void getPlayerForNewGame_createsNewPlayerIfNotFound() {
        PlayerInfo created = new PlayerInfo("Bob");

        when(repo.findByName("Bob")).thenReturn(Mono.empty());
        when(repo.save(any(PlayerInfo.class))).thenReturn(Mono.just(created));

        StepVerifier.create(service.getPlayerForNewGame("Bob"))
                .expectNext(created)
                .verifyComplete();
    }

    @Test
    void getPlayerRanking_returnsSortedRanking() {
        PlayerInfo p1 = new PlayerInfo("Alice");
        p1.setEarnings(new BigDecimal("200"));

        PlayerInfo p2 = new PlayerInfo("Bob");
        p2.setEarnings(new BigDecimal("100"));

        when(repo.findAll()).thenReturn(Flux.just(p2, p1));

        StepVerifier.create(service.getPlayerRanking())
                .expectNext(p1) // highest earnings first
                .expectNext(p2)
                .verifyComplete();
    }

    @Test
    void getPlayerRanking_throwsEmptyRankingException() {
        when(repo.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(service.getPlayerRanking())
                .expectError(EmptyRankingException.class)
                .verify();
    }

    @Test
    void updateName_updatesPlayerName() {
        UUID id = UUID.randomUUID();

        PlayerInfo existing = new PlayerInfo("OldName");
        existing.setId(id);

        PlayerInfo updated = new PlayerInfo("NewName");
        updated.setId(id);

        when(repo.findById(id)).thenReturn(Mono.just(existing));
        when(repo.save(existing)).thenReturn(Mono.just(updated));

        StepVerifier.create(service.updateName(id, "NewName"))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void updateName_throwsPlayerNotFoundException() {
        UUID id = UUID.randomUUID();

        when(repo.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(service.updateName(id, "NewName"))
                .expectError(PlayerNotFoundException.class)
                .verify();
    }

    @Test
    void updatePlayerEarnings_addsAmountCorrectly() {
        UUID id = UUID.randomUUID();

        PlayerInfo player = new PlayerInfo("Alice");
        player.setId(id);
        player.setEarnings(new BigDecimal("50"));

        PlayerInfo saved = new PlayerInfo("Alice");
        saved.setId(id);
        saved.setEarnings(new BigDecimal("80")); // 50 + 30

        when(repo.findById(id)).thenReturn(Mono.just(player));
        when(repo.save(player)).thenReturn(Mono.just(saved));

        StepVerifier.create(service.updatePlayerEarnings(id, new BigDecimal("30")))
                .verifyComplete();
    }

    @Test
    void updatePlayerEarnings_doesNothingIfPlayerNotFound() {
        UUID id = UUID.randomUUID();

        when(repo.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(service.updatePlayerEarnings(id, new BigDecimal("10")))
                .verifyComplete();
    }
}