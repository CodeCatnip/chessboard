package com.example.chessboard.entity;

import jakarta.persistence.Entity;

@Entity
public class Cannon extends Unit {
    public Cannon() {
        this.setType(UnitType.CANNON);
    }
}