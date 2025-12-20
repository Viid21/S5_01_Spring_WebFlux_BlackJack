package com.davidrey.blackjack.deck.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }
}
