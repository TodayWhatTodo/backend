package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.security.Authority;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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
    @Column
    private String nickname;
    @Column
    private String password;
    @Column
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
    private Boolean isAcceptAlarm;

    @Builder
    private User(String email, String nickname, String password, String name, Authority authority, String companyName, String imagePath, Boolean isAcceptAlarm) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.authority = authority;
        this.companyName = companyName;
        this.imagePath = imagePath;
        this.isAcceptAlarm = isAcceptAlarm;
    }
}
