package com.davidrey.blackjack.game.service;

import com.davidrey.blackjack.deck.model.Rank;
import com.davidrey.blackjack.game.model.Game;
import com.davidrey.blackjack.game.dto.PlayRequest;
import com.davidrey.blackjack.game.exception.IllegalMoveException;
import com.davidrey.blackjack.game.model.GameState;
import com.davidrey.blackjack.game.model.Hand;
import com.davidrey.blackjack.game.model.HandState;
import com.davidrey.blackjack.game.model.Winner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerActions {
    private final HandRules rules;

    public PlayerActions(HandRules rules) {
        this.rules = rules;
    }

    public void apply(Game game, PlayRequest request) {
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

    public void setInitialBet(Game game, BigDecimal amount) {
        if (game.getGameState() != GameState.NEED_INITIAL_BET) {
            throw new IllegalMoveException();
        }

        rules.validateBasicBet(amount);

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

    public void hit(Game game, PlayRequest request) {
        if (game.getGameState() != GameState.NEED_PLAYER_MOVE) {
            throw new IllegalMoveException();
        }

        Hand hand = game.getPlayerHands().get(request.handIndex());
        rules.validateHandPlayable(hand);

        hand.addCard(game.getDeck().draw());
    }

    public void stand(Game game, PlayRequest request) {
        if (game.getGameState() != GameState.NEED_PLAYER_MOVE) {
            throw new IllegalMoveException();
        }

        Hand hand = game.getPlayerHands().get(request.handIndex());
        rules.validateHandPlayable(hand);

        hand.setState(HandState.STAND);
    }

    public void split(Game game, PlayRequest request) {
        if (game.getGameState() != GameState.NEED_PLAYER_MOVE) {
            throw new IllegalMoveException();
        }

        Hand originalHand = game.getPlayerHands().get(request.handIndex());
        BigDecimal originalBet = originalHand.getBet();

        rules.validateSplit(originalHand, request.amount(), originalBet);

        Hand newHand = new Hand();
        newHand.setCards(new ArrayList<>(List.of(originalHand.getCards().getLast())));
        newHand.setState(HandState.ACTIVE);
        newHand.setBet(request.amount());

        originalHand.getCards().removeLast();
        originalHand.addCard(game.getDeck().draw());
        newHand.addCard(game.getDeck().draw());

        originalHand.setState(rules.splitAceVerification(originalHand));
        newHand.setState(rules.splitAceVerification(newHand));

        game.getPlayerHands().add(newHand);
    }

    public void dobleDown(Game game, PlayRequest request) {
        if (game.getGameState() != GameState.NEED_PLAYER_MOVE) {
            throw new IllegalMoveException();
        }

        Hand hand = game.getPlayerHands().get(request.handIndex());
        BigDecimal originalBet = hand.getBet();

        rules.validateDoubleDown(hand, request.amount(), originalBet);

        hand.setBet(hand.getBet().add(request.amount()));
        hand.addCard(game.getDeck().draw());
        hand.setState(HandState.STAND);
    }

    public void insurance(Game game, PlayRequest request) {
        if (game.getGameState() != GameState.OFFER_INSURANCE) {
            throw new IllegalMoveException();
        }

        Hand hand = game.getPlayerHands().get(request.handIndex());
        BigDecimal originalBet = hand.getBet();

        rules.validateInsurance(originalBet, request.amount());

        game.setInsurance(request.amount());
        game.setGameState(GameState.NEED_PLAYER_MOVE);
    }

    public void surrender(Game game, PlayRequest request) {
        if (game.getGameState() != GameState.NEED_PLAYER_MOVE) {
            throw new IllegalMoveException();
        }

        Hand hand = game.getPlayerHands().get(request.handIndex());
        rules.validateHandPlayable(hand);

        hand.setState(HandState.STAND);
        hand.setWinner(Winner.SURRENDER);
    }
}
