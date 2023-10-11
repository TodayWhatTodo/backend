package com.project.todayWhatToDo.notify.domain;

import com.project.todayWhatToDo.notify.dto.NotifyDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * userId 는 USER entity foreign key 로 사용된다.
 */
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Long userId;
    @CreatedDate
    @Column
    private LocalDateTime createdAt;
    @Column
    private Boolean isChecked;
    @Column
    private String content;

    @Builder
    private Notify(Long userId, String content) {
        this.userId = userId;
        this.content = content;
        this.isChecked = false;
    }

    public NotifyDto toDto(){
        return NotifyDto.builder()
                .content(content)
                .createdAt(createdAt)
                .id(id)
                .build();
    }

    public void check() {
        isChecked = true;
    }
}
