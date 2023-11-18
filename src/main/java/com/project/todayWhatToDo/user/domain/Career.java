package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.user.dto.request.UpdateCareerRequest;
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
    private Job job;
    @Column
    private String introduction;
    @Column(nullable = false)
    private LocalDateTime startedAt;
    @Column
    private LocalDateTime endedAt;

    @Builder
    private Career(User user, Job job, String introduction, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.user = user;
        this.job = job;
        this.introduction = introduction;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public void update(UpdateCareerRequest request) {
        if (request.startedAt() != null) startedAt = request.startedAt();
        if (request.endedAt() != null) endedAt = request.endedAt();
        if (request.introduction() != null) introduction = request.introduction();
        if (job != null) job.setPosition(request.position());

        validation();
    }

    private void validation(){
        if(endedAt != null && startedAt.isAfter(endedAt)) throw new IllegalArgumentException("입사일이 퇴사일보다 큽니다.");
    }
}