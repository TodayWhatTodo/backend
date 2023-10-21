package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Keyword;
import com.project.todayWhatToDo.post.dto.KeywordDto;
import com.project.todayWhatToDo.post.exception.KeywordNotFoundException;
import com.project.todayWhatToDo.post.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Keyword createKeyword(String keyword) {
        return keywordRepository.save(Keyword.from(keyword));
    }

    public void updateKeyword(KeywordDto keywordDto) {
        keywordRepository.findById(keywordDto.keywordId()).orElseThrow(KeywordNotFoundException::new)
                .update(keywordDto);
    }

//    public void deleteKeyword(KeywordDto keywordDto) {
//        keywordRepository.deleteById(keywordDto.keywordId());
//    }

//    public Page<KeywordDto> keywordList(Pageable pageable) {
//        return keywordRepository.findAll(pageable).map(Keyword::toDto);
//    }

    public Optional<Keyword> findByKeyword(String keyword) {
        return keywordRepository.findByKeyword(keyword);
    }

}
