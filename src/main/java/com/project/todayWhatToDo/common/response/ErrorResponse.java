package com.project.todayWhatToDo.common.response;

public record ErrorResponse(
        String status,
        String message
) {
}
