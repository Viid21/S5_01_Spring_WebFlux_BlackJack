package com.davidrey.blackjack.player.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table("player_info")
public class PlayerInfo {

    @Id
    private UUID id; // sense inicialitzar

    private String name;

    private BigDecimal earnings;

    public PlayerInfo(String name) {
        this.name = name;
        this.earnings = BigDecimal.ZERO;
    }
}
