package com.project.todayWhatToDo.common;

import com.project.todayWhatToDo.security.Authority;
import com.project.todayWhatToDo.security.JwtAuthentication;
import com.project.todayWhatToDo.security.JwtToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class JwtSecurityContextFactory implements WithSecurityContextFactory<WithCommonUser> {
    @Override
    public SecurityContext createSecurityContext(WithCommonUser annotation) {

        var userInfo = new JwtToken(annotation.userId(), annotation.nickname(), Authority.COMMON);
        var context = SecurityContextHolder.createEmptyContext();
        var auth = new JwtAuthentication(userInfo);

        context.setAuthentication(auth);
        return context;
    }
}
