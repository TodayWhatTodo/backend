package com.project.todayWhatToDo.user.repository;

import com.project.todayWhatToDo.user.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Page<Follow> findByFollowingId(Long userId, Pageable pageable);

    Page<Follow> findByFollowerId(Long userId, Pageable pageable);
}
