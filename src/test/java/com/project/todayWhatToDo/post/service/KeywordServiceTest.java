package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.post.dto.KeywordDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class KeywordServiceTest extends IntegrationTest {

    @Autowired
    KeywordService keywordService;

    @DisplayName("키워드 등록")
    @Test
    public void save_keyword() {
        // given
        var keywordDto = KeywordDto.builder()
                .keyword("키워드")
                .build();
        // when

        // then
    }

    @DisplayName("중복 추가 안됨")
    @Test
    public void duplicate() {

    }

    @DisplayName("키워드 수정")
    @Test
    public void update_keyword() {

    }

    @DisplayName("키워드 삭제")
    @Test
    public void delete_keyword() {

    }
}
