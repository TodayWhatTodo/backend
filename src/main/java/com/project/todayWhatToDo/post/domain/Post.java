package com.project.todayWhatToDo.post.domain;

import com.project.todayWhatToDo.BaseTimeEntity;
import com.project.todayWhatToDo.post.dto.PostRequestDto;
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
    private Integer likeCount = 0;

    @Column(length = 20)
    private String category;

    @Column(nullable = false)
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likeList = new ArrayList<>();


    public void addLike(User user) {
        likeCount++;
        likeList.add(Like.of(user, this));
    }

    public void increaseLike() {
        likeCount++;
    }

    public void decreaseLike() {
        likeCount--;
    }

    @Builder
    public Post(User user, String author, String title, String category, String content, PostStatus status) {
        this.user = user;
        this.author = author;
        this.title = title;
        this.likeCount = 0;
        this.category = category;
        this.content = content;
        this.status = status;
    }

    public void update(PostRequestDto requestDto) {
        if(requestDto.status() != null) this.status = requestDto.status();
        if(requestDto.content() != null) this.content = requestDto.content();
        if(requestDto.title() != null) this.title = requestDto.title();
        if(requestDto.category() != null) this.category = requestDto.category();
        if(requestDto.likeCount() != null) this.likeCount = requestDto.likeCount();

    }
}
