package com.davidrey.blackjack.player.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("player_info")
public class PlayerInfo {
    @Id
    private UUID id;
    private String name;
    private BigDecimal earnings;
}
