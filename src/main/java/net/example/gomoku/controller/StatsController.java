package net.example.gomoku.controller;

import net.example.gomoku.model.GameResult;
import net.example.gomoku.model.Player;
import net.example.gomoku.repository.GameRecordRepository;
import net.example.gomoku.repository.PlayerRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin
public class StatsController {

    private final PlayerRepository playerRepository;
    private final GameRecordRepository gameRecordRepository;

    public StatsController(PlayerRepository playerRepository, GameRecordRepository gameRecordRepository) {
        this.playerRepository = playerRepository;
        this.gameRecordRepository = gameRecordRepository;
    }

    @PostMapping("/save")
    public void saveResult(@RequestBody GameResult result) {
        result.setTimestamp(LocalDateTime.now());
        gameRecordRepository.save(result);

        // Оновлення гравця
        Optional<Player> existing = playerRepository.findByName(result.getWinner());
        if (existing.isPresent()) {
            Player p = existing.get();
            int currentWins = p.getWins(); // підстрахуємось, якщо null
            p.setWins(currentWins + 1);
            playerRepository.save(p);
        } else {
            Player newPlayer = new Player(result.getWinner());
            newPlayer.setWins(1); // встановлюємо першу перемогу
            playerRepository.save(newPlayer);
        }

    }

    @GetMapping("/leaders")
    public List<Player> getLeaderboard() {
        return playerRepository.findAllByOrderByWinsDesc();
    }

    @GetMapping("/history")
    public List<GameResult> getGameHistory() {
        return gameRecordRepository.findAllByOrderByTimestampDesc();
    }
}
