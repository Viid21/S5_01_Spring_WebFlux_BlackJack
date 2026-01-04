package com.davidrey.blackjack.game.exception;

public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException() {
        super("Player action not accepted.");
    }
}
