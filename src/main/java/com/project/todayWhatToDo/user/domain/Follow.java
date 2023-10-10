package com.project.todayWhatToDo.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false, name = "followerId")
    private User follower;
    @ManyToOne
    @JoinColumn(nullable = false, name = "followingId")
    private User following;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @Builder
    private Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}
