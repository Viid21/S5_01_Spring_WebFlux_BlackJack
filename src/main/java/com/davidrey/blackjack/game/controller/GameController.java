package com.davidrey.blackjack.game.controller;

import com.davidrey.blackjack.game.dto.GameResponse;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.mapper.GameControllerMapper;
import com.davidrey.blackjack.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Game created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponse.class)
                    )
            )
    })
    @PostMapping(
            value = "/new",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<GameResponse>> newGame(@RequestBody String playerName) {
        return service.createNewGame(playerName)
                .map(gameResult -> {
                    GameResponse dto = mapper.toDto(gameResult);
                    URI uri = URI.create("/game/" + gameResult.getId());
                    return ResponseEntity.created(uri).body(dto);
                });
    }

    @Operation(
            summary = "Get game state",
            description = "Returns the current state of the game with the specified ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Game retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<GameResponse> getGame(@PathVariable UUID id) {
        return service.getGameById(id)
                .map(mapper::toDto);
    }

    @Operation(
            summary = "Make a move",
            description = "Allows the player to perform a move such as HIT, STAND, SPLIT, DOUBLE, INSURANCE, or SURRENDER."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Move applied successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid move or invalid bet",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping(
            value = "/{id}/play",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<GameResponse> makeMove(@PathVariable UUID id, @RequestBody PlayRequest request) {
        return service.play(id, request)
                .map(mapper::toDto);
    }

    @Operation(
            summary = "Delete a game",
            description = "Deletes the game with the specified ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Game deleted successfully",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @DeleteMapping(
            value = "/{id}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable UUID id) {
        return service.deleteGameById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
