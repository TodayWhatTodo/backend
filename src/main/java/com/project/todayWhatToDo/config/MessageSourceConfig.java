package com.project.todayWhatToDo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class MessageSourceConfig {
    @Bean
    public LocaleResolver localeResolver(){
        return new AcceptHeaderLocaleResolver();
    }
}
