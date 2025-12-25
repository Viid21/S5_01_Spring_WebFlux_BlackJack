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
@RequiredArgsConstructor
@Table("player_info")
public class PlayerInfo {
    @Id
    private UUID id = UUID.randomUUID();
    @NonNull
    private String name;
    private BigDecimal earnings = BigDecimal.valueOf(0);
}
