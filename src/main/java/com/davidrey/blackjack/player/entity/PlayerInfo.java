package com.davidrey.blackjack.player.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("player_info")
public class PlayerInfo {
    @Id
    private UUID id;
    private String name;
    private String email;
    private int phone;
}
