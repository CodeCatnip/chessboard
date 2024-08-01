package com.example.chessboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UnitType type;

    private String color;
    private int x;
    private int y;
    private boolean destroyed = false;
    private int moves = 0;
    private long lastCommandTimestamp = 0;
}
