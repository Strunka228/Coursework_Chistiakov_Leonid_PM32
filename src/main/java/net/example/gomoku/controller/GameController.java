package net.example.gomoku.controller;

import net.example.gomoku.model.Game;
import net.example.gomoku.model.MoveRequest;
import net.example.gomoku.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")  // ← змінено
@CrossOrigin
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Game getGame() {
        return gameService.getGame();
    }

    @PostMapping("/move")
    public Game makeMove(@RequestBody MoveRequest move) {
        gameService.makeMove(move);
        return gameService.getGame();
    }

    @PostMapping("/save-result")
    public void saveResultManually() {
        gameService.saveGameIfValid();  // 👈 зроби цей метод public
    }



    @PostMapping("/new")
    public Game newGame(
            @RequestParam("mode") String mode,
            @RequestParam("opponent") String opponent,
            @RequestParam("playerX") String playerX,
            @RequestParam("playerO") String playerO) {

        gameService.startNewGame(
                Game.Mode.valueOf(mode),
                Game.OpponentType.valueOf(opponent),
                playerX,
                playerO
        );
        return gameService.getGame();
    }

}
