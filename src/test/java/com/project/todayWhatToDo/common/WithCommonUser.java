package com.project.todayWhatToDo.common;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = JwtSecurityContextFactory.class)
public @interface WithCommonUser {
    long userId();
    String nickname();
}
