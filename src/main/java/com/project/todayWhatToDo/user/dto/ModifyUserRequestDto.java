package com.project.todayWhatToDo.user.dto;

public record ModifyUserRequestDto(
        Long id,
        String nickname,
        String introduction,
        String companyName
) {
}
