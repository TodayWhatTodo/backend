package com.project.todayWhatToDo.post.domain;

import com.project.todayWhatToDo.BaseTimeEntity;
import com.project.todayWhatToDo.post.dto.KeywordDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Keyword extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String keyword;

    @Builder
    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    public KeywordDto toDto() {
        return KeywordDto.builder()
                .keyword(keyword)
                .build();
    }

    public void update(KeywordDto requestDto) {
        if(requestDto.keyword() != null) this.keyword = requestDto.keyword();

    }
}
