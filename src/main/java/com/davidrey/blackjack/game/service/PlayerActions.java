package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.deck.model.Rank;
import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.exception.IllegalMoveException;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Hand;
import com.davidrey.blackjack.game.model.HandState;
import com.davidrey.blackjack.game.model.Winner;

import java.math.BigDecimal;
import java.util.List;

public class PlayerActions {
    private final HandRules rules;

    public PlayerActions(HandRules rules) {
        this.rules = rules;
    }

    public void apply(GameInfo game, PlayRequest request) {
        switch (request.move()) {
            case INITIAL_BET -> setInitialBet(game, request.amount());
            case HIT -> hit(game, request);
            case STAND -> stand(game, request);
            case SPLIT -> split(game, request);
            case DOBLE_DOWN -> dobleDown(game, request);
            case INSURANCE -> insurance(game, request);
            case SURRENDER -> surrender(game, request);
        }
    }

    public void setInitialBet(GameInfo game, BigDecimal amount) {
        rules.validateBasicBet(amount);

        if (!game.getPlayerHands().isEmpty()) {
            throw new IllegalMoveException();
        }

        Hand playerHand = new Hand();
        playerHand.setBet(amount);

        playerHand.addCard(game.getDeck().draw());
        game.getDealerHand().addCard(game.getDeck().draw());
        playerHand.addCard(game.getDeck().draw());

        playerHand.validateNatBj();

        playerHand.setState(HandState.ACTIVE);
        game.getPlayerHands().add(playerHand);

        if (game.getDealerHand().getCards().getFirst().rank() == Rank.ACE) {
            game.setGameState(GameState.OFFER_INSURANCE);
        } else {
            game.setGameState(GameState.NEED_PLAYER_MOVE);
        }
    }

    public void hit(GameInfo game, PlayRequest request) {
        Hand hand = game.getPlayerHands().get(request.handIndex());
        rules.validateHandPlayable(hand);

        hand.addCard(game.getDeck().draw());
    }

    public void stand(GameInfo game, PlayRequest request) {
        Hand hand = game.getPlayerHands().get(request.handIndex());
        rules.validateHandPlayable(hand);

        hand.setState(HandState.STAND);
    }

    public void split(GameInfo game, PlayRequest request) {
        Hand originalHand = game.getPlayerHands().get(request.handIndex());
        BigDecimal originalBet = originalHand.getBet();

        rules.validateSplit(originalHand, request.amount(), originalBet);

        Hand newHand = new Hand(
                List.of(originalHand.getCards().getLast()),
                HandState.ACTIVE,
                request.amount()
        );

        originalHand.getCards().removeLast();
        originalHand.addCard(game.getDeck().draw());
        newHand.addCard(game.getDeck().draw());

        originalHand.setState(rules.splitAceVerification(originalHand));
        newHand.setState(rules.splitAceVerification(newHand));

        game.getPlayerHands().add(newHand);
    }

    public void dobleDown(GameInfo game, PlayRequest request) {
        Hand hand = game.getPlayerHands().get(request.handIndex());
        BigDecimal originalBet = hand.getBet();

        rules.validateDoubleDown(hand, request.amount(), originalBet);

        hand.setBet(hand.getBet().add(request.amount()));
        hand.addCard(game.getDeck().draw());
        hand.setState(HandState.STAND);
    }

    public void insurance(GameInfo game, PlayRequest request) {
        Hand hand = game.getPlayerHands().get(request.handIndex());
        BigDecimal originalBet = hand.getBet();

        rules.validateInsurance(game.getGameState(), originalBet, request.amount());

        game.setInsurance(request.amount());
    }

    public void surrender(GameInfo game, PlayRequest request) {
        Hand hand = game.getPlayerHands().get(request.handIndex());
        rules.validateHandPlayable(hand);

        hand.setWinner(Winner.SURRENDER);
    }
}
