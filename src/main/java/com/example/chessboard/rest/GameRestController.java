package com.example.chessboard.rest;

import com.example.chessboard.entity.Command;
import com.example.chessboard.entity.Unit;
import com.example.chessboard.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameRestController {

    private GameService gameService;

    @Autowired
    public GameRestController(GameService gameService){
        this.gameService=gameService;
    }

    @GetMapping("/units")
    public List<Unit> getUnits() {
        return gameService.getUnits();
    }

    @PostMapping("/command")
    public void executeCommand(@RequestBody Command command) {
        gameService.executeCommand(command);
    }

    @PostMapping("/random-command")
    public ResponseEntity<Void> executeRandomCommand(@RequestParam String side, @RequestParam Long unitId) {
        if (!gameService.isUnitBelongsToSide(unitId, side)) {
            return ResponseEntity.badRequest().build();
        }
        gameService.executeRandomCommand(side, unitId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new-game")
    public void newGame() {
        gameService.newGame();
    }
}
