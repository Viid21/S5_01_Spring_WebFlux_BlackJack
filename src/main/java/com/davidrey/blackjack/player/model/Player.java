package com.davidrey.blackjack.player.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Player {
    //sql
    private UUID id = UUID.randomUUID();
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private Integer phone;

    //mongo
    @NonNull
    private Integer BankAmmount;
    private int wins;
    private int totalGames;
    private int earnings;
}
