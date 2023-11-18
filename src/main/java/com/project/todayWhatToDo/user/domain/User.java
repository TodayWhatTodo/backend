package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.security.Authority;
import com.project.todayWhatToDo.user.dto.response.ProfileResponse;
import com.project.todayWhatToDo.user.dto.request.UpdateUserSettingRequest;
import com.project.todayWhatToDo.user.dto.UserSession;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.todayWhatToDo.security.Authority.QUIT;

@Table(name = "USERS")
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nickname;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column
    @CreatedDate
    private LocalDateTime visitedAt;
    @Enumerated(EnumType.STRING)
    @Column
    private Authority authority;
    @Embedded
    private Job job;
    @Column
    private String imagePath;
    @Column
    private String introduction;
    @Column
    private Integer followerCount;
    @Column
    private Integer followingCount;
    @Column
    private Boolean isAcceptAlarm;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers = new ArrayList<>();
    @OneToMany(mappedBy = "follower")
    private List<Follow> followers = new ArrayList<>();
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();


    @Builder
    private User(String email, String nickname, String password, String name, Authority authority, Job job, String imagePath, String introduction, Boolean isAcceptAlarm) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.authority = authority;
        this.imagePath = imagePath;
        this.job = job;
        this.introduction = introduction;
        this.followerCount = 0;
        this.followingCount = 0;
        this.isAcceptAlarm = isAcceptAlarm;
    }

    public void addCareer(Career career) {
        careers.add(career);
    }

    public List<Career> getCareers() {
        return careers.stream().toList();
    }

    public List<Follow> getFollowers() {
        return followers.stream().toList();
    }

    public List<Follow> getFollowings() {
        return followings.stream().toList();
    }

    public void setNickname(String nickname) {
        if (nickname != null) this.nickname = nickname;
    }

    public void setIntroduction(String introduction) {
        if (introduction != null) this.introduction = introduction;
    }

    public void setJob(Job job) {
        if (job != null && job.getCompanyName() != null) this.job = job;
    }

    public void setImagePath(String imagePath) {
        if (imagePath != null) this.imagePath = imagePath;
    }

    public UserSession toSession() {
        return UserSession.builder()
                .email(email)
                .name(name)
                .id(getId())
                .build();
    }

    private void addFollower(Follow follow) {
        followerCount++;
        followers.add(follow);
    }

    public void addFollowing(User follower) {
        this.followingCount++;
        Follow follow = Follow.builder()
                .following(this)
                .follower(follower)
                .build();

        followings.add(follow);
        follower.addFollower(follow);
    }

    public void reduceFollower() {
        followerCount--;
    }

    public void reduceFollowing() {
        followingCount--;
    }

    public ProfileResponse toProfile() {
        return ProfileResponse.builder()
                .profileImagePath(imagePath)
                .company(job.getCompanyName())
                .position(job.getPosition())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .introduction(introduction)
                .nickname(nickname)
                .userId(id)
                .build();
    }

    public void quit() {
        authority = QUIT;
    }

    public void setting(UpdateUserSettingRequest request) {
        isAcceptAlarm = request.isAcceptAlarm();
    }
}
