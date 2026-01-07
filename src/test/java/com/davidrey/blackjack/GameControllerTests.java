package com.davidrey.blackjack;

import com.davidrey.blackjack.deck.Shoe;
import com.davidrey.blackjack.exception.GlobalExceptionHandler;
import com.davidrey.blackjack.game.controller.GameController;
import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.GameResponse;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.exception.GameNotFoundException;
import com.davidrey.blackjack.game.mapper.GameControllerMapper;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Hand;
import com.davidrey.blackjack.game.model.PlayerMove;
import com.davidrey.blackjack.game.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class GameControllerTests {

    private GameService service;
    private GameControllerMapper mapper;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        service = Mockito.mock(GameService.class);
        mapper = Mockito.mock(GameControllerMapper.class);

        GameController controller = new GameController(service, mapper);
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void newGame_createsNewGameAndReturns201() {
        String playerName = "Alice";
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        GameInfo gameInfo = new GameInfo(playerId, GameState.NEED_INITIAL_BET, new Shoe(6));
        gameInfo.setId(gameId);

        GameResponse response = new GameResponse(
                GameState.NEED_INITIAL_BET,
                new ArrayList<>(),
                new Hand()
        );

        when(service.createNewGame(playerName)).thenReturn(Mono.just(gameInfo));
        when(mapper.toDto(gameInfo)).thenReturn(response);

        webTestClient.post()
                .uri("/game/new")
                .bodyValue(playerName)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/game/" + gameId)
                .expectBody(GameResponse.class)
                .isEqualTo(response);
    }

    @Test
    void getGame_returnsGameWhenExists() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        GameInfo gameInfo = new GameInfo(playerId, GameState.NEED_INITIAL_BET, new Shoe(6));
        gameInfo.setId(gameId);

        GameResponse response = new GameResponse(
                GameState.NEED_INITIAL_BET,
                new ArrayList<>(),
                new Hand()
        );

        when(service.getGameById(gameId)).thenReturn(Mono.just(gameInfo));
        when(mapper.toDto(gameInfo)).thenReturn(response);

        webTestClient.get()
                .uri("/game/{id}", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameResponse.class)
                .isEqualTo(response);
    }

    @Test
    void getGame_returnsErrorWhenGameDoesNotExist() {
        UUID gameId = UUID.randomUUID();

        when(service.getGameById(gameId))
                .thenReturn(Mono.error(new GameNotFoundException(gameId)));

        webTestClient.get()
                .uri("/game/{id}", gameId)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void makeMove_executesMoveAndReturnsGameResponse() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        GameInfo gameInfo = new GameInfo(playerId, GameState.NEED_PLAYER_MOVE, new Shoe(6));
        gameInfo.setId(gameId);

        PlayRequest request = new PlayRequest(PlayerMove.HIT, 0, BigDecimal.ONE);

        GameResponse response = new GameResponse(
                GameState.NEED_PLAYER_MOVE,
                new ArrayList<>(),
                new Hand()
        );

        when(service.play(eq(gameId), any(PlayRequest.class)))
                .thenReturn(Mono.just(gameInfo));
        when(mapper.toDto(gameInfo)).thenReturn(response);

        webTestClient.post()
                .uri("/game/{id}/play", gameId)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameResponse.class)
                .isEqualTo(response);
    }

    @Test
    void makeMove_returnsErrorWhenGameDoesNotExist() {
        UUID gameId = UUID.randomUUID();
        PlayRequest request = new PlayRequest(PlayerMove.HIT, 0, BigDecimal.ONE);

        when(service.play(eq(gameId), any(PlayRequest.class)))
                .thenReturn(Mono.error(new GameNotFoundException(gameId)));

        webTestClient.post()
                .uri("/game/{id}/play", gameId)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deleteGame_deletesGameAndReturns204() {
        UUID gameId = UUID.randomUUID();

        when(service.deleteGameById(gameId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/game/{id}/delete", gameId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteGame_returnsErrorWhenGameDoesNotExist() {
        UUID gameId = UUID.randomUUID();

        when(service.deleteGameById(gameId))
                .thenReturn(Mono.error(new GameNotFoundException(gameId)));

        webTestClient.delete()
                .uri("/game/{id}/delete", gameId)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
