package com.davidrey.blackjack.player.exception;

public class EmptyRankingException extends RuntimeException {
    public EmptyRankingException() {
        super("No one played yet.");
    }
}
