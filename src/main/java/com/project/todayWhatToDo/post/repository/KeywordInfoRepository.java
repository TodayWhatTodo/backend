package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.KeywordInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordInfoRepository extends JpaRepository<KeywordInfo, Long> {

     List<KeywordInfo> findByPostId(Long postId);
}
