package net.example.gomoku.service;

import net.example.gomoku.model.Game;
import net.example.gomoku.model.GameResult;
import net.example.gomoku.model.MoveRequest;
import net.example.gomoku.model.Player;
import net.example.gomoku.repository.GameRecordRepository;
import net.example.gomoku.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
public class GameService {

    private final PlayerRepository playerRepository;
    private final GameRecordRepository gameRecordRepository;

    private Game game;
    private long startTime;
    private static final long TIME_LIMIT = 60000; // 60 секунд

    public GameService(PlayerRepository playerRepository, GameRecordRepository gameRecordRepository) {
        this.playerRepository = playerRepository;
        this.gameRecordRepository = gameRecordRepository;
    }

    public void startNewGame(Game.Mode mode, Game.OpponentType opponentType, String playerX, String playerO) {
        game = new Game();
        game.setMode(mode);
        game.setOpponentType(opponentType);
        game.setPlayerX(playerX);
        game.setPlayerO(playerO);
        game.setCurrentPlayer("X");
        game.setMoves(new HashMap<>());
        game.setFinished(false);
        game.setWinner(null);
        game.setScoreX(0);
        game.setScoreO(0);
        if (mode == Game.Mode.TIME_LIMITED) {
            startTime = System.currentTimeMillis();
        }
    }

    public Game getGame() {
        return game;
    }

    public void makeMove(MoveRequest move) {
        if (game.isFinished()) return;

        if (game.getMode() == Game.Mode.TIME_LIMITED &&
                System.currentTimeMillis() - startTime > TIME_LIMIT) {
            game.setFinished(true);
            game.setWinner(getWinnerByScore());

            // Зберігаємо результат, якщо не нічия і не CPU
            String winner = game.getWinner();
            if (!"Нічия".equals(winner) && game.getOpponentType() != Game.OpponentType.CPU) {
                saveGameIfValid();
            }

            return;
        }


        int row = move.getX();
        int col = move.getY();
        String key = row + "," + col;
        if (game.getMoves().containsKey(key)) return;

        String player = game.getCurrentPlayer();
        game.getMoves().put(key, player);

        if (checkWin(row, col, player)) {
            if (game.getMode() == Game.Mode.TIME_LIMITED) {
                if (player.equals("X")) game.setScoreX(game.getScoreX() + 1);
                else game.setScoreO(game.getScoreO() + 1);
            } else {
                game.setFinished(true);
                game.setWinner(player);
                saveGameIfValid();
                return;
            }
        }

        game.setCurrentPlayer(player.equals("X") ? "O" : "X");

        if (!game.isFinished() && game.getOpponentType() == Game.OpponentType.CPU && player.equals("X")) {
            makeComputerMove();
        }
    }

    public void saveGameIfValid() {
        if (game.getOpponentType() == Game.OpponentType.CPU) return;
        if ("Нічия".equals(game.getWinner())) return;

        String winnerName = game.getWinner().equals("X") ? game.getPlayerX() : game.getPlayerO();

        GameResult result = new GameResult(
                game.getPlayerX(),
                game.getPlayerO(),
                winnerName,
                LocalDateTime.now()
        );
        gameRecordRepository.save(result);

        Optional<Player> existing = playerRepository.findByName(winnerName);
        if (existing.isPresent()) {
            Player p = existing.get();
            p.setWins(p.getWins() + 1);
            playerRepository.save(p);
        } else {
            playerRepository.save(new Player(winnerName, 1));
        }
    }

    private String getWinnerByScore() {
        if (game.getScoreX() > game.getScoreO()) return "X";
        else if (game.getScoreO() > game.getScoreX()) return "O";
        else return "Нічия";
    }

    private void makeComputerMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                String key = row + "," + col;
                if (!game.getMoves().containsKey(key)) {
                    game.getMoves().put(key, "O");
                    int score = evaluateBoard("O") - evaluateBoard("X");
                    game.getMoves().remove(key);

                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = row;
                        bestCol = col;
                    }
                }
            }
        }

        if (bestRow != -1 && bestCol != -1) {
            String key = bestRow + "," + bestCol;
            game.getMoves().put(key, "O");
            if (checkWin(bestRow, bestCol, "O")) {
                game.setFinished(true);
                game.setWinner("O");
                saveGameIfValid();
            } else {
                game.setCurrentPlayer("X");
            }
        }
    }

    private int evaluateBoard(String player) {
        int score = 0;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                String key = row + "," + col;
                if (player.equals(game.getMoves().get(key))) {
                    score += evaluateDirection(row, col, 0, 1, player);
                    score += evaluateDirection(row, col, 1, 0, player);
                    score += evaluateDirection(row, col, 1, 1, player);
                    score += evaluateDirection(row, col, 1, -1, player);
                }
            }
        }
        return score;
    }

    private int evaluateDirection(int row, int col, int dx, int dy, String player) {
        int count = 0;
        for (int i = 1; i < 5; i++) {
            int r = row + dx * i;
            int c = col + dy * i;
            String key = r + "," + c;
            if (player.equals(game.getMoves().get(key))) {
                count++;
            } else {
                break;
            }
        }
        return (int) Math.pow(10, count);
    }

    private boolean checkWin(int row, int col, String player) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int count = 1;
            count += countDirection(row, col, dir[0], dir[1], player);
            count += countDirection(row, col, -dir[0], -dir[1], player);
            if (count >= 5) return true;
        }
        return false;
    }

    public void finalizeTimedGame() {
        if (!game.isFinished()) {
            game.setFinished(true);
            game.setWinner(getWinnerByScore());
            saveGameIfValid();
        }
    }


    private int countDirection(int row, int col, int dx, int dy, String player) {
        int count = 0;
        for (int i = 1; i < 5; i++) {
            int newRow = row + dx * i;
            int newCol = col + dy * i;
            String key = newRow + "," + newCol;
            if (player.equals(game.getMoves().getOrDefault(key, ""))) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
