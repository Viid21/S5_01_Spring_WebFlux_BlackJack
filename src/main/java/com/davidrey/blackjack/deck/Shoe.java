package com.davidrey.blackjack.deck;

import com.davidrey.blackjack.deck.model.Card;
import com.davidrey.blackjack.deck.model.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shoe {
    private final List<Card> cards = new ArrayList<>();

    public Shoe(int numOfDecks) {
        for (int i = 0; i < numOfDecks; i++) {
            Deck deck = new Deck();
            cards.addAll(deck.getCards());
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        return cards.removeFirst();
    }

    public int size() {
        return cards.size();
    }
}
