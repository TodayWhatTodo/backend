package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"keywords"})
    Optional<Post> findFetchById(Long postId);
}
