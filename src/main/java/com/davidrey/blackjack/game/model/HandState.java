package com.davidrey.blackjack.game.model;

public enum HandState {
    ACTIVE, STAND, BUST, NAT_BJ;

    public boolean isFinished() {
        return this == STAND || this == BUST || this == NAT_BJ;
    }
}
