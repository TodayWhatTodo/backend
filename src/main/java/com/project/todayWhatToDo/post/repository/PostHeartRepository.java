package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostHeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByPostIdAndUserId(Long postId, Long UserId);
}
