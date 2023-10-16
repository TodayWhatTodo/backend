package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndUserId(Long postId, Long UserId);
}
