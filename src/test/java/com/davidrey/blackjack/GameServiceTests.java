package com.davidrey.blackjack;

import com.davidrey.blackjack.deck.Shoe;
import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.exception.GameNotFoundException;
import com.davidrey.blackjack.game.mapper.GameServiceMapper;
import com.davidrey.blackjack.game.model.Game;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.PlayerMove;
import com.davidrey.blackjack.game.repository.GameRepository;
import com.davidrey.blackjack.game.service.GameCrupier;
import com.davidrey.blackjack.game.service.GameLogic;
import com.davidrey.blackjack.game.service.GameService;
import com.davidrey.blackjack.player.entity.PlayerInfo;
import com.davidrey.blackjack.player.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GameServiceTests {

    private GameRepository repo;
    private PlayerService playerService;
    private GameLogic logic;
    private GameCrupier crupier;
    private GameServiceMapper mapper;
    private GameService service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(GameRepository.class);
        playerService = Mockito.mock(PlayerService.class);
        logic = Mockito.mock(GameLogic.class);
        crupier = Mockito.mock(GameCrupier.class);
        mapper = Mockito.mock(GameServiceMapper.class);
        service = new GameService(repo, playerService, logic, crupier, mapper);
    }

    @Test
    void createNewGame_createsNewGameSuccessfully() {
        String playerName = "Alice";
        UUID playerId = UUID.randomUUID();
        PlayerInfo playerInfo = new PlayerInfo(playerName);
        playerInfo.setId(playerId);

        GameInfo gameInfo = new GameInfo(playerId, GameState.NEED_INITIAL_BET, new Shoe(6));
        UUID gameId = UUID.randomUUID();
        gameInfo.setId(gameId);

        when(playerService.getPlayerForNewGame(playerName)).thenReturn(Mono.just(playerInfo));
        when(repo.save(any(GameInfo.class))).thenReturn(Mono.just(gameInfo));

        StepVerifier.create(service.createNewGame(playerName))
                .expectNextMatches(game -> 
                    game.getPlayerId().equals(playerId) &&
                    game.getGameState() == GameState.NEED_INITIAL_BET
                )
                .verifyComplete();
    }

    @Test
    void play_executesMoveAndSavesGame() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        
        GameInfo gameInfo = new GameInfo(playerId, GameState.NEED_INITIAL_BET, new Shoe(6));
        gameInfo.setId(gameId);

        Game game = new Game();
        game.setId(gameId);
        game.setPlayerId(playerId);
        game.setGameState(GameState.NEED_PLAYER_MOVE);

        GameInfo savedGameInfo = new GameInfo(playerId, GameState.NEED_PLAYER_MOVE, new Shoe(6));
        savedGameInfo.setId(gameId);

        PlayRequest request = new PlayRequest(PlayerMove.HIT, 0, BigDecimal.ONE);

        when(repo.findById(gameId)).thenReturn(Mono.just(gameInfo));
        when(mapper.toGame(gameInfo)).thenReturn(game);
        when(logic.play(game, request)).thenReturn(game);
        when(mapper.toInfo(game)).thenReturn(savedGameInfo);
        when(repo.save(savedGameInfo)).thenReturn(Mono.just(savedGameInfo));

        StepVerifier.create(service.play(gameId, request))
                .expectNext(savedGameInfo)
                .verifyComplete();
    }

    @Test
    void play_updatesPlayerEarningsWhenGameFinished() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        
        GameInfo gameInfo = new GameInfo(playerId, GameState.NEED_PLAYER_MOVE, new Shoe(6));
        gameInfo.setId(gameId);

        Game game = new Game();
        game.setId(gameId);
        game.setPlayerId(playerId);
        game.setGameState(GameState.FINISHED);

        GameInfo savedGameInfo = new GameInfo(playerId, GameState.FINISHED, new Shoe(6));
        savedGameInfo.setId(gameId);

        PlayRequest request = new PlayRequest(PlayerMove.STAND, 0, null);
        BigDecimal earnings = new BigDecimal("50");

        when(repo.findById(gameId)).thenReturn(Mono.just(gameInfo));
        when(mapper.toGame(gameInfo)).thenReturn(game);
        when(logic.play(game, request)).thenReturn(game);
        when(crupier.calculateEarnings(game)).thenReturn(earnings);
        when(playerService.updatePlayerEarnings(playerId, earnings)).thenReturn(Mono.empty());
        when(mapper.toInfo(game)).thenReturn(savedGameInfo);
        when(repo.save(savedGameInfo)).thenReturn(Mono.just(savedGameInfo));

        StepVerifier.create(service.play(gameId, request))
                .expectNext(savedGameInfo)
                .verifyComplete();
    }

    @Test
    void play_throwsGameNotFoundExceptionWhenGameDoesNotExist() {
        UUID gameId = UUID.randomUUID();
        PlayRequest request = new PlayRequest(PlayerMove.HIT, 0, BigDecimal.ONE);

        when(repo.findById(gameId)).thenReturn(Mono.empty());

        StepVerifier.create(service.play(gameId, request))
                .expectError(GameNotFoundException.class)
                .verify();
    }

    @Test
    void getGameById_returnsGameWhenExists() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        
        GameInfo gameInfo = new GameInfo(playerId, GameState.NEED_INITIAL_BET, new Shoe(6));
        gameInfo.setId(gameId);

        when(repo.findById(gameId)).thenReturn(Mono.just(gameInfo));

        StepVerifier.create(service.getGameById(gameId))
                .expectNext(gameInfo)
                .verifyComplete();
    }

    @Test
    void getGameById_throwsGameNotFoundExceptionWhenGameDoesNotExist() {
        UUID gameId = UUID.randomUUID();

        when(repo.findById(gameId)).thenReturn(Mono.empty());

        StepVerifier.create(service.getGameById(gameId))
                .expectError(GameNotFoundException.class)
                .verify();
    }

    @Test
    void deleteGameById_deletesGameWhenExists() {
        UUID gameId = UUID.randomUUID();

        when(repo.existsById(gameId)).thenReturn(Mono.just(true));
        when(repo.deleteById(gameId)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteGameById(gameId))
                .verifyComplete();
    }

    @Test
    void deleteGameById_throwsGameNotFoundExceptionWhenGameDoesNotExist() {
        UUID gameId = UUID.randomUUID();

        when(repo.existsById(gameId)).thenReturn(Mono.just(false));

        StepVerifier.create(service.deleteGameById(gameId))
                .expectError(GameNotFoundException.class)
                .verify();
    }
}
