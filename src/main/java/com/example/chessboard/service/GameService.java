package com.example.chessboard.service;

import com.example.chessboard.config.BoardConfig;
import com.example.chessboard.dao.CommandRepository;
import com.example.chessboard.dao.UnitHistoryRepository;
import com.example.chessboard.dao.UnitRepository;
import com.example.chessboard.entity.*;
import com.example.chessboard.util.UnitFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class GameService {

    private UnitRepository unitRepository;
    private CommandRepository commandRepository;
    private UnitHistoryRepository unitHistoryRepository;
    private UnitFactory unitFactory;
    private int boardWidth;
    private int boardHeight;

    @Autowired
    public GameService(UnitRepository unitRepository, CommandRepository commandRepository, UnitHistoryRepository unitHistoryRepository, UnitFactory unitFactory, BoardConfig boardConfig) {
        this.unitRepository = unitRepository;
        this.commandRepository = commandRepository;
        this.unitHistoryRepository = unitHistoryRepository;
        this.unitFactory = unitFactory;
        this.boardWidth = boardConfig.getWidth();
        this.boardHeight = boardConfig.getHeight();
    }


    public List<Unit> getUnits() {
        return unitRepository.findAll();
    }

    @Transactional
    public void executeCommand(Command command) {
        Optional<Unit> optionalUnit = unitRepository.findById(command.getUnitId());
        if (optionalUnit.isPresent()) {
            Unit unit = optionalUnit.get();
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - unit.getLastCommandTimestamp();

            boolean canExecute = false;
            switch (command.getType()) {
                case MOVE:
                    String[] parts = command.getDetails().split(" ");
                    String direction = parts[0];
                    int distance = Integer.parseInt(parts[1]);
                    canExecute = (unit.getType() == UnitType.ARCHER && timeDiff >= 5000 && distance==1) ||
                            (unit.getType() == UnitType.TRANSPORT && timeDiff >= 7000 && distance>=1 && distance<=3);
                    if (canExecute) {
                        moveUnit(unit, direction, distance);
                        unit.setLastCommandTimestamp(currentTime);
                        unit.setMoves(unit.getMoves() + 1);
                        unitRepository.save(unit);
                        saveUnitHistory(unit);
                    }
                    break;
                case SHOOT:
                    canExecute = (unit.getType() == UnitType.ARCHER && timeDiff >= 10000) ||
                            (unit.getType() == UnitType.CANNON && timeDiff >= 13000);
                    if (canExecute) {
                        shoot(unit, command.getDetails());
                        unit.setLastCommandTimestamp(currentTime);
                        unit.setMoves(unit.getMoves() + 1);
                        unitRepository.save(unit);
                        saveUnitHistory(unit);
                    }
                    break;
            }
            if (canExecute) {
                command.setTimestamp(LocalDateTime.now());
                commandRepository.save(command);
            }
        }
    }

    @Transactional
    public void executeRandomCommand(String side, Long unitId) {
        Optional<Unit> optionalUnit = unitRepository.findById(unitId);
        if (optionalUnit.isPresent()) {
            Unit unit = optionalUnit.get();
            Command command = new Command();
            command.setSide(side);
            command.setUnitId(unitId);
            command.setTimestamp(LocalDateTime.now());

            Random random = new Random();

            if (unit.getType() == UnitType.ARCHER) {
                if (random.nextBoolean()) {
                    command.setType(CommandType.MOVE);
                    command.setDetails(randomMove(1)); // Archers can only move 1 step
                } else {
                    command.setType(CommandType.SHOOT);
                    command.setDetails(randomShoot()); // Archers can shoot with variable distance
                }
            } else if (unit.getType() == UnitType.TRANSPORT) {
                int distance = random.nextInt(3) + 1; // Generates a distance from 1 to 3
                command.setType(CommandType.MOVE);
                command.setDetails(randomMove(distance));
            } else if (unit.getType() == UnitType.CANNON) {
                command.setType(CommandType.SHOOT);
                command.setDetails(randomShoot()); // Assuming cannon shooting can be random
            }
            executeCommand(command);
        }
    }

    public boolean isUnitBelongsToSide(Long unitId, String side) {
        Unit unit = unitRepository.findById(unitId).orElse(null);
        return unit != null && unit.getColor().equals(side);
    }

    private String randomMove(int maxDistance) {
        Random random = new Random();
        String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"};
        String direction = directions[random.nextInt(directions.length)];
        int distance = random.nextInt(maxDistance) + 1;
        return direction + " " + distance;
    }


    private String randomShoot() {
        Random random = new Random();
        int xDistance = random.nextInt(5) + 1;
        int yDistance = random.nextInt(5) + 1;

        boolean shootHorizontal = random.nextBoolean();

        int x = shootHorizontal ? xDistance : 0;
        int y = shootHorizontal ? 0 : yDistance;

        return x + " " + y;
    }

    private void moveUnit(Unit unit, String direction, int distance) {
        int[] newPosition = calculateNewPosition(unit.getX(), unit.getY(), direction, distance);
        int newX = newPosition[0];
        int newY = newPosition[1];

        if (isWithinBoard(newX, newY) && !checkCollision(unit, newX, newY)) {
            unit.setX(newX);
            unit.setY(newY);
            unitRepository.save(unit);
        }
    }


    private int[] calculateNewPosition(int x, int y, String direction, int distance) {
        int newX = x;
        int newY = y;

        switch (direction) {
            case "UP":
                newY = y - distance;
                break;
            case "DOWN":
                newY = y + distance;
                break;
            case "LEFT":
                newX = x - distance;
                break;
            case "RIGHT":
                newX = x + distance;
                break;
        }

        return new int[]{newX, newY};
    }

    private boolean isWithinBoard(int x, int y) {
        return x >= 0 && x < boardWidth && y >= 0 && y < boardHeight;
    }

    private boolean checkCollision(Unit unit, int newX, int newY) {
        List<Unit> units = unitRepository.findAll();
        for (Unit target : units) {
            if (target.getX() == newX && target.getY() == newY) {
                if (target.getColor().equals(unit.getColor())) {
                    return true;
                } else {
                    target.setDestroyed(true);
                    unitRepository.save(target);
                    saveUnitHistory(target);
                }
            }
        }
        return false;
    }

    private void shoot(Unit unit, String coordinates) {
        String[] coords = coordinates.split(" ");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        List<Unit> units = unitRepository.findAll();

        for (Unit target : units) {
            if (target.getX() == unit.getX() + x && target.getY() == unit.getY() + y) {
                target.setDestroyed(true);
                unitRepository.save(target);
                saveUnitHistory(target);
            }
        }
    }

    private void saveUnitHistory(Unit unit) {
        UnitHistory unitHistory = new UnitHistory();
        unitHistory.setUnitId(unit.getId());
        unitHistory.setX(unit.getX());
        unitHistory.setY(unit.getY());
        unitHistory.setDestroyed(unit.isDestroyed());
        unitHistory.setTimestamp(LocalDateTime.now());
        unitHistoryRepository.save(unitHistory);
    }

    @Transactional
    public void newGame() {
        unitRepository.deleteAll();
        List<Unit> units = unitFactory.createUnits(4, 2, 5);
        unitRepository.saveAll(units);
    }
}
