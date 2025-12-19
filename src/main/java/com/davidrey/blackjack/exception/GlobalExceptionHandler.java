package com.davidrey.blackjack.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlayerNotFoundException.class)
    public Mono<ResponseEntity<String>> handlePlayerNotFound(PlayerNotFoundException ex) {
        return Mono.just(ResponseEntity.status(404).body(ex.getMessage()));
    }
}
