package com.project.todayWhatToDo.security;

public record JwtToken(
        Long userId,
        String nickname,
        Authority authority
) {
}
