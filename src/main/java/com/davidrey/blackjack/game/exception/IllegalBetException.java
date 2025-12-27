package com.davidrey.blackjack.game.exception;

public class IllegalBetException extends RuntimeException {
    public IllegalBetException() {
        super("Initial bet not accepted.");
    }
}
