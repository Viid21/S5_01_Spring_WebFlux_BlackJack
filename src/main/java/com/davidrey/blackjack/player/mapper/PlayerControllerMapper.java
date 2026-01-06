package com.davidrey.blackjack.player.mapper;

import com.davidrey.blackjack.player.dto.PlayerDto;
import com.davidrey.blackjack.player.entity.PlayerInfo;
import org.springframework.stereotype.Component;

@Component
public class PlayerControllerMapper {
    public PlayerDto toDto(PlayerInfo player) {
        return new PlayerDto(
                player.getId(),
                player.getName(),
                player.getEarnings()
        );
    }
}
