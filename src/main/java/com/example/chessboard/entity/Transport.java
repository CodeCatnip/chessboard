package com.example.chessboard.entity;

import jakarta.persistence.Entity;

@Entity
public class Transport extends Unit {
    public Transport() {
        this.setType(UnitType.TRANSPORT);
    }
}