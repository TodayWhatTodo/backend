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
    private Integer likeCount;

    @Column(length = 20)
    private String category;

    @Column(nullable = false)
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    public void addPost(User user) {
        this.likePosts.add(LikePost.builder()
                .user(user)
                .post(this)
                .build());
    }


    @Builder
    public Post(String author, String title, Integer like, String category, String content, PostStatus status) {
        this.author = author;
        this.title = title;
        this.likeCount = like;
        this.category = category;
        this.content = content;
        this.postStatus = status;
    }


    public void update(PostRequestDto requestDto) {
        this.postStatus = requestDto.getPostStatus();
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.category = requestDto.getCategory();
        this.likeCount = requestDto.getLikeCount();
    }
}
