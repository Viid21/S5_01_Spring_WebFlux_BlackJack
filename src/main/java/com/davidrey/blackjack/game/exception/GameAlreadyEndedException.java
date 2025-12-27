package com.davidrey.blackjack.game.exception;

import java.util.UUID;

public class GameAlreadyEndedException extends RuntimeException {
    public GameAlreadyEndedException(UUID id) {
        super("This game already ended, game with id: " + id);
    }
}
