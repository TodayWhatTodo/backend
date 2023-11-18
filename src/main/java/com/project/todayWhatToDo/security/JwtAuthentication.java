package com.project.todayWhatToDo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JwtAuthentication implements Authentication {

    private boolean isAuthenticated;
    private final JwtToken principal;

    public JwtAuthentication(JwtToken principal) {
        this.isAuthenticated = true;
        this.principal = principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(principal.authority().name()));
    }

    @Override
    public Object getCredentials() {
        return principal.authority();
    }

    @Override
    public Object getDetails() {
        return principal.nickname();
    }

    @Override
    public JwtToken getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal.nickname();
    }
}
