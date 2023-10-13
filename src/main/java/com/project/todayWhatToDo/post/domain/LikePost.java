package com.project.todayWhatToDo.post.domain;

import com.project.todayWhatToDo.BaseTimeEntity;
import com.project.todayWhatToDo.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class LikePost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @Builder
    public LikePost(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public static LikePost of(User user, Post post) {
        return LikePost.builder()
                .post(post)
                .user(user)
                .build();
    }
}
