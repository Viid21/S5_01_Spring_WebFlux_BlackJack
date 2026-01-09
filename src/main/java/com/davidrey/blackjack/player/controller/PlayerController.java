package com.davidrey.blackjack.player.controller;

import com.davidrey.blackjack.player.dto.PlayerDto;
import com.davidrey.blackjack.player.mapper.PlayerControllerMapper;
import com.davidrey.blackjack.player.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@Tag(name = "Players", description = "Operations related to player management")
public class PlayerController {
    private final PlayerService service;
    private final PlayerControllerMapper mapper;

    public PlayerController(PlayerService service, PlayerControllerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Get player ranking",
            description = "Returns a list of players ordered by their earnings."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Ranking retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Empty ranking"
    )
    @GetMapping("/ranking")
    public Flux<PlayerDto> getRanking() {
        return service.getPlayerRanking()
                .map(mapper::toDto);
    }

    @Operation(
            summary = "Update a player's name",
            description = "Updates the name of the player with the specified ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Player name updated successfully",
            content = @Content(schema = @Schema(implementation = PlayerDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Player not found",
            content = @Content(schema = @Schema(hidden = true))
    )
    @PutMapping(
            value = "/player/{id}",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<PlayerDto> updatePlayerName(@PathVariable UUID id, @RequestBody String name) {
        return service.updateName(id, name)
                .map(mapper::toDto);
    }
}
