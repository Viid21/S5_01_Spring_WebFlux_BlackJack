package com.davidrey.blackjack.player.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "player_stats")
public class PlayerStats {
    @Id
    private UUID playerId;
    private int BankAmmount;
    private int wins;
    private int totalGames;
    private int earnings;
}
