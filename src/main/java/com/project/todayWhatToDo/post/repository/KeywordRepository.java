package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
