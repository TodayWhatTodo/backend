package com.project.todayWhatToDo.user.dto.request;

import lombok.Builder;

@Builder
public record CreateUserRequest(
        String provider,
        String token,
        String nickname,
        String companyName,
        String introduction,
        Boolean isAcceptAlarm,
        String imagePath
        ) {
}
