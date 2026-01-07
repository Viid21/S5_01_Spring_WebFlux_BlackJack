package com.davidrey.blackjack;

import com.davidrey.blackjack.player.controller.PlayerController;
import com.davidrey.blackjack.player.dto.PlayerDto;
import com.davidrey.blackjack.player.entity.PlayerInfo;
import com.davidrey.blackjack.player.mapper.PlayerControllerMapper;
import com.davidrey.blackjack.player.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;

class PlayerControllerTests {

    private PlayerService service;
    private PlayerControllerMapper mapper;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        service = Mockito.mock(PlayerService.class);
        mapper = Mockito.mock(PlayerControllerMapper.class);

        PlayerController controller = new PlayerController(service, mapper);

        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void getRanking_returnsPlayerDtoList() {
        PlayerInfo p1 = new PlayerInfo("Alice");
        PlayerInfo p2 = new PlayerInfo("Bob");

        PlayerDto dto1 = new PlayerDto(p1.getId(), p1.getName(), p1.getEarnings());
        PlayerDto dto2 = new PlayerDto(p2.getId(), p2.getName(), p2.getEarnings());

        when(service.getPlayerRanking()).thenReturn(Flux.just(p1, p2));
        when(mapper.toDto(p1)).thenReturn(dto1);
        when(mapper.toDto(p2)).thenReturn(dto2);

        webTestClient.get()
                .uri("/ranking")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlayerDto.class)
                .hasSize(2)
                .contains(dto1, dto2);
    }

    @Test
    void updatePlayerName_returnsUpdatedPlayerDto() {
        UUID id = UUID.randomUUID();
        String newName = "Charlie";

        PlayerInfo updated = new PlayerInfo(newName);
        PlayerDto updatedDto = new PlayerDto(id, newName, BigDecimal.valueOf(200));

        when(service.updateName(id, newName)).thenReturn(Mono.just(updated));
        when(mapper.toDto(updated)).thenReturn(updatedDto);

        webTestClient.put()
                .uri("/player/{id}", id)
                .bodyValue(newName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class)
                .isEqualTo(updatedDto);
    }
}