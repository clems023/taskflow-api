package com.taskflow.repository;

import com.taskflow.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, UUID> {

    List<BoardColumn> findByProjectIdOrderByPositionAsc(UUID projectId);
}
