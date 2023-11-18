package com.project.todayWhatToDo.user.dto.request;

import lombok.Builder;

@Builder
public record ModifyUserRequest(
        Long id,
        String nickname,
        String introduction,
        String companyName,
        String companyAddress,
        String imagePath
) {
}
