package com.example.chessboard.entity;

import jakarta.persistence.Entity;

@Entity
public class Archer extends Unit {
    public Archer() {
        this.setType(UnitType.ARCHER);
    }
}