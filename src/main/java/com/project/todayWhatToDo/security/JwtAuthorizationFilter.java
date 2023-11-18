package com.project.todayWhatToDo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        JwtToken userInfo = jwtService.getUserInfo(bearerToken);

        if (userInfo != null) {
            var auth = new JwtAuthentication(userInfo);
            SecurityContextHolder.getContext()
                    .setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
