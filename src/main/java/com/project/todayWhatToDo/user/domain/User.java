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
    @Embedded
    private Company company;
    @Column
    private String imagePath;
    @Column
    private String introduction;
    @Column
    private Boolean isAcceptAlarm;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers = new ArrayList<>();

    @Builder
    private User(String email, String nickname, String password, String name, Authority authority, Company company, String imagePath, String introduction, Boolean isAcceptAlarm) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.authority = authority;
        this.imagePath = imagePath;
        this.company = company;
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

    public void setCompany(Company company) {
        if (company != null && company.getName() != null) this.company = company;
    }

    public UserSession toSession() {
        return UserSession.builder()
                .email(email)
                .name(name)
                .id(getId())
                .build();
    }
}
