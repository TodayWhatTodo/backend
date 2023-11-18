package com.project.todayWhatToDo.security;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
    // todo
    public JwtToken getUserInfo(String token) {
        return new JwtToken(1L, "testuser", Authority.COMMON);
    }
}
