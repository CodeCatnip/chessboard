package com.example.chessboard.dao;

import com.example.chessboard.entity.UnitHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitHistoryRepository extends JpaRepository<UnitHistory, Long> {
}
