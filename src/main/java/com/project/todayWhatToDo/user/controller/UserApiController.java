package com.project.todayWhatToDo.user.controller;

import com.project.todayWhatToDo.common.response.RootResponse;
import com.project.todayWhatToDo.user.dto.response.ProfileResponse;
import com.project.todayWhatToDo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(
            @RequestHeader(name = "Accept-Language", required = false) Locale locale,
            @PathVariable Long userId) {
        ProfileResponse profile = userService.getProfile(userId);
        return ResponseEntity.ok(new RootResponse(successMessage(locale), profile));
    }

    private String successMessage(Locale locale) {
        return messageSource.getMessage("api.status.success", null, locale);
    }
}
