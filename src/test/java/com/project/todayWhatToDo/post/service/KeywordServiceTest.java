package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class KeywordServiceTest extends IntegrationTest {



    @DisplayName("키워드 등록")
    @Test
    public void save_keyword() {
        // given

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
