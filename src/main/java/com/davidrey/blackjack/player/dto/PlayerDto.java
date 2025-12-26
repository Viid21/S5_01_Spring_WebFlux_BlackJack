package com.davidrey.blackjack.player.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PlayerDto(UUID id, String name, BigDecimal earnings) {
}
