package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.user.dto.UpdateCareerRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Embedded
    private Company company;
    @Column
    private String introduction;
    @Column(nullable = false)
    private LocalDateTime startedAt;
    @Column
    private LocalDateTime endedAt;
    @Column
    private String position;

    @Builder
    private Career(User user, Company company, String introduction, LocalDateTime startedAt, LocalDateTime endedAt, String position) {
        this.user = user;
        this.company = company;
        this.introduction = introduction;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.position = position;
    }

    public void update(UpdateCareerRequestDto request) {
        if (request.startedAt() != null) startedAt = request.startedAt();
        if (request.endedAt() != null) endedAt = request.endedAt();
        if (request.introduction() != null) introduction = request.introduction();
        if (request.position() != null) position = request.position();

        validation();
    }

    private void validation(){
        if(endedAt != null && startedAt.isAfter(endedAt)) throw new IllegalArgumentException("입사일이 퇴사일보다 큽니다.");
    }
}
