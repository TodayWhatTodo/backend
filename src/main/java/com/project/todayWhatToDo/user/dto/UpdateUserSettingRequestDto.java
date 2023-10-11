package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record UpdateUserSettingRequestDto(
        Boolean isAcceptAlarm
) {
}
