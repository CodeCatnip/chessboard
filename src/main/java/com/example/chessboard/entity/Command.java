package com.example.chessboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String side;

    @Column(name = "unit_id")
    private Long unitId;

    private CommandType type;
    private String details;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "unit_id", insertable = false, updatable = false)
    private Unit unit;
}