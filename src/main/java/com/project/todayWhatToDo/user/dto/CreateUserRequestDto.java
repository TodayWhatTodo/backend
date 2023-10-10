package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record CreateUserRequestDto(
        String provider,
        String token,
        String nickname,
        String companyName,
        String introduction,
        Boolean isAcceptAlarm,
        String imagePath
        ) {
}
