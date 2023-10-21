package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Keyword;
import com.project.todayWhatToDo.post.domain.KeywordInfo;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.repository.KeywordInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class KeywordInfoService {

    private final KeywordService keywordService;
    private final KeywordInfoRepository keywordInfoRepository;

    public void saveKeyword(Post post, List<String> keywordList) {
        if (keywordList.isEmpty()) {
            return;
        }
        keywordList.stream()
                .map(keyword -> keywordService.findByKeyword(keyword)
                        .orElseGet(() -> keywordService.createKeyword(keyword)))
                .forEach(keyword -> mappingToPost(post, keyword));
    }

    public List<KeywordInfo> findByPostId(Long postId) {
        return keywordInfoRepository.findByPostId(postId);
    }

    private void mappingToPost(Post post, Keyword keyword) {
        keywordInfoRepository.save(KeywordInfo.of(post, keyword));
    }


}
