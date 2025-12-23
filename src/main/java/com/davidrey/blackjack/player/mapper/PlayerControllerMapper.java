package com.davidrey.blackjack.player.mapper;

import com.davidrey.blackjack.player.dto.PlayerDto;
import com.davidrey.blackjack.player.entity.PlayerInfo;
import reactor.core.publisher.Flux;

public class PlayerControllerMapper {
    public Flux<PlayerDto> toFluxDto(Flux<PlayerInfo> players) {
        return players.map(player -> new PlayerDto(
                player.getName(),
                player.getEarnings()
        ));
    }
}
