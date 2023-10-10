package com.project.todayWhatToDo.user.dto;

public record ModifyUserRequest(
        Long id,
        String nickname,
        String introduction
) {
}
