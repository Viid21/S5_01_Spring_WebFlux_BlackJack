package com.davidrey.blackjack.game.dto;

import com.davidrey.blackjack.game.model.PlayerMove;

import java.math.BigDecimal;

public record PlayRequest(
        PlayerMove move,
        Integer handIndex,
        BigDecimal amount) {
}
