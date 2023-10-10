package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.security.Authority;
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
    @Column
    private String companyName;
    @Column
    private String imagePath;
    @Column
    private String introduction;
    @Column
    private Boolean isAcceptAlarm;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers = new ArrayList<>();

    @Builder
    private User(String email, String nickname, String password, String name, Authority authority, String companyName, String imagePath, String introduction, Boolean isAcceptAlarm) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.authority = authority;
        this.companyName = companyName;
        this.imagePath = imagePath;
        this.introduction = introduction;
        this.isAcceptAlarm = isAcceptAlarm;
    }

    public void addCareer(Career career) {
        careers.add(career);
    }

    public List<Career> getCareers() {
        return careers.stream().toList();
    }

    public void setNickname(String nickname) {
        if (nickname != null) this.nickname = nickname;
    }

    public void setIntroduction(String introduction) {
        if (introduction != null) this.introduction = introduction;
    }

    public void setCompanyName(String companyName) {
        if (companyName != null) this.companyName = companyName;
    }

    public UserSession toSession() {
        return UserSession.builder()
                .email(email)
                .name(name)
                .id(getId())
                .build();
    }
}
