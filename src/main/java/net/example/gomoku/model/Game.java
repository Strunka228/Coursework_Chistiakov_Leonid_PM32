package net.example.gomoku.model;

import java.util.HashMap;
import java.util.Map;

public class Game {
    public enum Mode {
        FIRST_TO_FIVE, TIME_LIMITED
    }
    public enum OpponentType {
        HUMAN, CPU
    }
    private long timeRemaining = 60; // у секундах, опціонально
    public long getTimeRemaining() { return timeRemaining; }
    public void setTimeRemaining(long timeRemaining) { this.timeRemaining = timeRemaining; }

    private OpponentType opponentType = OpponentType.HUMAN;

    public OpponentType getOpponentType() {
        return opponentType;
    }

    public void setOpponentType(OpponentType opponentType) {
        this.opponentType = opponentType;
    }


    private Map<String, String> moves = new HashMap<>(); // "x,y" → "X"/"O"
    private String currentPlayer = "X";
    private Mode mode = Mode.FIRST_TO_FIVE;
    private boolean finished = false;
    private int scoreX = 0;
    private int scoreO = 0;
    private String winner;
    private String playerX;
    private String playerO;

    public Game() {}

    public Map<String, String> getMoves() {
        return moves;
    }

    public void setMoves(Map<String, String> moves) {
        this.moves = moves;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getScoreX() {
        return scoreX;
    }

    public void setScoreX(int scoreX) {
        this.scoreX = scoreX;
    }

    public int getScoreO() {
        return scoreO;
    }

    public void setScoreO(int scoreO) {
        this.scoreO = scoreO;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void togglePlayer() {
        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
    }

    public String getPlayerX() {
        return playerX;
    }

    public String getPlayerO() {
        return playerO;
    }

    public void setPlayerX(String playerX) {
        this.playerX = playerX;
    }

    public void setPlayerO(String playerO) {
        this.playerO = playerO;
    }
}
