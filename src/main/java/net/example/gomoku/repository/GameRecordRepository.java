package net.example.gomoku.repository;

import net.example.gomoku.model.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRecordRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findAllByOrderByTimestampDesc();
}
