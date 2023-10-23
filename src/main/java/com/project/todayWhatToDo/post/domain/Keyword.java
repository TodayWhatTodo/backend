package com.project.todayWhatToDo.post.domain;

import com.project.todayWhatToDo.BaseTimeEntity;
import com.project.todayWhatToDo.post.dto.KeywordRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@Entity
public class Keyword extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String keyword;

    @Builder
    public Keyword(Post post, String keyword) {
        this.post = post;
        this.keyword = keyword;
    }

    public static Keyword from(KeywordRequestDto requestDto) {
        return Keyword.builder()
                .keyword(requestDto.after().isBlank() ? requestDto.keyword() : requestDto.after())
                .build();
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

