package com.davidrey.blackjack.game.mapper;

import com.davidrey.blackjack.game.document.GameInfo;
import com.davidrey.blackjack.game.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameServiceMapper {
    public Game toGame (GameInfo info){
        Game game = new Game();
        game.setGameState(info.getGameState());
        game.setId(info.getId());
        game.setDeck(info.getDeck());
        game.setInsurance(info.getInsurance());
        game.setDealerHand(info.getDealerHand());
        game.setPlayerHands(info.getPlayerHands());
        game.setPlayerId(info.getPlayerId());
        return game;
    }

    public GameInfo toInfo (Game game){
        GameInfo info = new GameInfo();
        info.setGameState(game.getGameState());
        info.setId(game.getId());
        info.setDeck(game.getDeck());
        info.setInsurance(game.getInsurance());
        info.setDealerHand(game.getDealerHand());
        info.setPlayerHands(game.getPlayerHands());
        info.setPlayerId(game.getPlayerId());
        return info;
    }
}
