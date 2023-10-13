package com.project.todayWhatToDo.post.domain;

import com.project.todayWhatToDo.BaseTimeEntity;
import com.project.todayWhatToDo.post.dto.request.PostRequestDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<LikePost> likePosts = new ArrayList<>();

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 20, nullable = false)
    private String author;

    @Column
    private Integer like;

    @Column(length = 20)
    private String category;

    @Column(nullable = false)
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private PostStatus status;


    public void addLikePost(User user) {
        this.likePosts.add(LikePost.builder()
                .user(user)
                .post(this)
                .build());
    }


    @Builder
    public Post(User user, String author, String title, Integer like, String category, String content, PostStatus status) {
        this.user = user;
        this.author = author;
        this.title = title;
        this.like = like;
        this.category = category;
        this.content = content;
        this.status = status;
    }

    public void update(PostRequestDto requestDto) {
        if(requestDto.getStatus() != null) this.status = requestDto.getStatus();
        if(requestDto.getContent() != null) this.content = requestDto.getContent();
        if(requestDto.getTitle() != null) this.title = requestDto.getTitle();
        if(requestDto.getCategory() != null) this.category = requestDto.getCategory();
        if(requestDto.getLike() != null) this.like = requestDto.getLike();

    }
}
