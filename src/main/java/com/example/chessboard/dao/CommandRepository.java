package com.example.chessboard.dao;

import com.example.chessboard.entity.Command;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepository extends JpaRepository<Command, Long> {
}
