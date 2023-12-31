package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Page<Keyword> findAll(Pageable pageable);

    Optional<Keyword> findByKeywordAndPostId(String keyword, Long postId);

    List<Keyword> findByPostId(Long postId);
}
