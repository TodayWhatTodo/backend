package com.project.todayWhatToDo.user.controller;

import com.project.todayWhatToDo.common.response.RootResponse;
import com.project.todayWhatToDo.security.JwtToken;
import com.project.todayWhatToDo.user.dto.response.ProfileResponse;
import com.project.todayWhatToDo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<?> getUserProfile(
            @RequestHeader(name = "Accept-Language", required = false) Locale locale
    ) {
        JwtToken status = getStatus();
        ProfileResponse profile = userService.getProfile(status.userId());
        return ResponseEntity.ok(new RootResponse(successMessage(locale), profile));
    }

    private static JwtToken getStatus() {
        return (JwtToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String successMessage(Locale locale) {
        return messageSource.getMessage("api.status.success", null, locale);
    }
}
