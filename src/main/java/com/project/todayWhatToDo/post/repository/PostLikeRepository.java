package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<LikePost, Long> {

    Optional<LikePost> findByPostIdAndUserId(Long postId, Long UserId);
}
