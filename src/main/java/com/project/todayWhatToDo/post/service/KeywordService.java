package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Keyword;
import com.project.todayWhatToDo.post.dto.KeywordDto;
import com.project.todayWhatToDo.post.exception.KeywordNotFoundException;
import com.project.todayWhatToDo.post.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public void createKeyword(KeywordDto keywordDto) {
        keywordRepository.save(keywordDto.toEntity());
    }

    public void updateKeyword(KeywordDto keywordDto) {
        keywordRepository.findById(keywordDto.keywordId()).orElseThrow(KeywordNotFoundException::new).update(keywordDto);
    }

    public void deleteKeyword(KeywordDto keywordDto) {
        keywordRepository.deleteById(keywordDto.keywordId());
    }

    public Page<KeywordDto> keywordList(Pageable pageable) {
        return keywordRepository.findAll(pageable).map(Keyword::toDto);
    }

}
