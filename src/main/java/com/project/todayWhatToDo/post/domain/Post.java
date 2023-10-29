package com.project.todayWhatToDo.post.domain;

import com.project.todayWhatToDo.BaseTimeEntity;
import com.project.todayWhatToDo.post.dto.UpdatePostRequestDto;
import com.project.todayWhatToDo.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 20, nullable = false)
    private String author;

    @Column
    private Integer heartCount = 0;

    @Column(length = 20)
    private String category;

    @Column(nullable = false)
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywords = new ArrayList<>();

    @Builder
    private Post(String title, String author, String category, String content, PostStatus status, User user, List<Heart> heartList, List<String> keywords) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.content = content;
        this.status = status;
        keywords.forEach(this::addKeyword);
    }

    public void addKeyword(String name) {
        keywords.add(Keyword.builder()
                .post(this)
                .keyword(name)
                .build()
        );
    }

    public void addLike(User user) {
        heartCount++;
        heartList.add(Heart.of(user, this));
    }

    public void increaseLike() {
        heartCount++;
    }

    public void decreaseLike() {
        heartCount--;
    }


    public void update(UpdatePostRequestDto requestDto) {
        if (requestDto.status() != null) this.status = requestDto.status();
        if (requestDto.content() != null) this.content = requestDto.content();
        if (requestDto.title() != null) this.title = requestDto.title();
        if (requestDto.category() != null) this.category = requestDto.category();
        if(!keywords.isEmpty()) {
            keywords.clear();
            requestDto.keywordList().forEach(this::addKeyword);
        }
    }

    public void update() {

    }

}
