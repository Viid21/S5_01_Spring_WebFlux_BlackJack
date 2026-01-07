package com.davidrey.blackjack.game.controller;

import com.davidrey.blackjack.game.dto.GameResponse;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.mapper.GameControllerMapper;
import com.davidrey.blackjack.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/game")
@Tag(name = "Blackjack Game", description = "Endpoints for managing blackjack games")
public class GameController {
    private final GameService service;
    private final GameControllerMapper mapper;

    public GameController(GameService service, GameControllerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Create a new game",
            description = "Starts a new blackjack game for the specified player."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Game created successfully",
            content = @Content(schema = @Schema(implementation = GameResponse.class))
    )
    @PostMapping("/new")
    public Mono<ResponseEntity<GameResponse>> newGame(@RequestBody String playerName) {
        return service.createNewGame(playerName)
                .flatMap(gameResult -> {
                    GameResponse dto = mapper.toDto(gameResult);
                    URI uri = URI.create("/game/" + gameResult.getId());
                    return Mono.just(ResponseEntity.created(uri).body(dto));
                });
    }

    @Operation(
            summary = "Get game state",
            description = "Returns the current state of the game with the specified ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Game retrieved successfully",
            content = @Content(schema = @Schema(implementation = GameResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Game not found"
    )
    @GetMapping("/{id}")
    public Mono<GameResponse> getGame(@PathVariable UUID id) {
        return service.getGameById(id)
                .map(mapper::toDto);
    }

    @Operation(
            summary = "Make a move",
            description = "Allows the player to perform a move such as HIT, STAND, SPLIT, DOUBLE, INSURANCE, or SURRENDER."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Move applied successfully",
            content = @Content(schema = @Schema(implementation = GameResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid move"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid bet"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Game not found"
    )
    @PostMapping("/{id}/play")
    public Mono<GameResponse> makeMove(@PathVariable UUID id, @RequestBody PlayRequest request) {
        return service.play(id, request)
                .map(mapper::toDto);
    }

    @Operation(
            summary = "Delete a game",
            description = "Deletes the game with the specified ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Game deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Game not found"
    )
    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable UUID id) {
        return service.deleteGameById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
