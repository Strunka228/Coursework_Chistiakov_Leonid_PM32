package net.example.gomoku.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int wins;

    // Конструктори, геттери і сеттери
    public Player() {}

    public Player(String name) {
        this.name = name;
        this.wins = 0;
    }
    public Player(String name, int wins) {
        this.name = name;
        this.wins = wins;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

}

