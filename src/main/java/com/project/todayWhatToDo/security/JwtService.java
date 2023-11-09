package com.project.todayWhatToDo.security;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
    // todo
    public JwtToken getToken(String token) {
        return new JwtToken(1L);
    }
}
