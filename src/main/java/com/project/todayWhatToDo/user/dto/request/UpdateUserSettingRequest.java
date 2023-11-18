package com.project.todayWhatToDo.user.dto.request;

import lombok.Builder;

@Builder
public record UpdateUserSettingRequest(
        Boolean isAcceptAlarm
) {
}
