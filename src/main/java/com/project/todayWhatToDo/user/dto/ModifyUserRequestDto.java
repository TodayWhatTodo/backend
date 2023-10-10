package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record ModifyUserRequestDto(
        Long id,
        String nickname,
        String introduction,
        String companyName,
        String companyAddress,
        String imagePath
) {
}
