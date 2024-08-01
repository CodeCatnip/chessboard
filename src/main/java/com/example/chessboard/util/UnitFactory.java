package com.example.chessboard.util;

import com.example.chessboard.config.BoardConfig;
import com.example.chessboard.entity.Archer;
import com.example.chessboard.entity.Cannon;
import com.example.chessboard.entity.Transport;
import com.example.chessboard.entity.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UnitFactory {

    private BoardConfig boardConfig;

    @Autowired
    public UnitFactory(BoardConfig boardConfig) {
        this.boardConfig = boardConfig;
    }

    public List<Unit> createUnits(int numArchersPerSide, int numCannonsPerSide, int numTransportsPerSide) {
        List<Unit> units = new ArrayList<>();
        Random random = new Random();
        int boardWidth = boardConfig.getWidth();
        int boardHeight = boardConfig.getHeight();
        String white = "white";
        String black = "black";

        // Track occupied positions to avoid collisions
        Set<String> occupiedPositions = new HashSet<>();

        // Create units for both sides
        for (String color : new String[]{white, black}) {
            for (int i = 0; i < numArchersPerSide; i++) {
                units.add(createUnit(color, Archer.class, random, boardWidth, boardHeight, occupiedPositions));
            }

            for (int i = 0; i < numCannonsPerSide; i++) {
                units.add(createUnit(color, Cannon.class, random, boardWidth, boardHeight, occupiedPositions));
            }

            for (int i = 0; i < numTransportsPerSide; i++) {
                units.add(createUnit(color, Transport.class, random, boardWidth, boardHeight, occupiedPositions));
            }
        }
        return units;
    }

    private Unit createUnit(String color, Class<? extends Unit> unitClass, Random random, int boardWidth, int boardHeight, Set<String> occupiedPositions) {
        try {
            Unit unit = unitClass.getDeclaredConstructor().newInstance();
            unit.setColor(color);

            // Find a unique position for the unit
            int x, y;
            String position;
            do {
                x = random.nextInt(boardWidth);
                y = random.nextInt(boardHeight);
                position = x + "," + y;
            } while (occupiedPositions.contains(position));

            unit.setX(x);
            unit.setY(y);
            occupiedPositions.add(position);

            return unit;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create unit", e);
        }
    }
}