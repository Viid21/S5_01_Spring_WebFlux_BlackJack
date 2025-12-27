package com.davidrey.blackjack.exception;

import com.davidrey.blackjack.game.exception.GameAlreadyEndedException;
import com.davidrey.blackjack.game.exception.GameNotFoundException;
import com.davidrey.blackjack.game.exception.IllegalBetException;
import com.davidrey.blackjack.player.exception.EmptyRankingException;
import com.davidrey.blackjack.player.exception.PlayerNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlayerNotFoundException.class)
    public Mono<ResponseEntity<String>> handlePlayerNotFound(PlayerNotFoundException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(EmptyRankingException.class)
    public Mono<ResponseEntity<String>> handleEmptyRanking(EmptyRankingException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(GameNotFoundException.class)
    public Mono<ResponseEntity<String>> handleGameNotFound(GameNotFoundException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(GameAlreadyEndedException.class)
    public Mono<ResponseEntity<String>> handleGameAlreadyEnded(GameAlreadyEndedException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(IllegalBetException.class)
    public Mono<ResponseEntity<String>> handleIllegalBet(IllegalBetException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }
}
