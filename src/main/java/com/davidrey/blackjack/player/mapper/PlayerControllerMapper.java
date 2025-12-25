package com.davidrey.blackjack.player.mapper;

import com.davidrey.blackjack.player.dto.PlayerDto;
import com.davidrey.blackjack.player.entity.PlayerInfo;

public class PlayerControllerMapper {
    public PlayerDto toDto(PlayerInfo player) {
        return new PlayerDto(
                player.getName(),
                player.getEarnings()
        );
    }
}
