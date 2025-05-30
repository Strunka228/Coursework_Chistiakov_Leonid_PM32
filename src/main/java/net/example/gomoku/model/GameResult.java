package net.example.gomoku.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerX;
    private String playerO;
    private String winner;
    private LocalDateTime timestamp;

    // Конструктори, геттери і сеттери
    public GameResult() {}

    public GameResult(String playerX, String playerO, String winner, LocalDateTime timestamp) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.winner = winner;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getPlayerX() {
        return playerX;
    }

    public String getPlayerO() {
        return playerO;
    }

    public String getWinner() {
        return winner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayerX(String playerX) {
        this.playerX = playerX;
    }

    public void setPlayerO(String playerO) {
        this.playerO = playerO;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

