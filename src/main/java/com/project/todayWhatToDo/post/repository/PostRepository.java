package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
