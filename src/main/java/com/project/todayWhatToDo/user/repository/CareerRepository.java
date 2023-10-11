package com.project.todayWhatToDo.user.repository;

import com.project.todayWhatToDo.user.domain.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Long> {
    Optional<Career> findByIdAndUserId(Long careerId, Long userId);
}
