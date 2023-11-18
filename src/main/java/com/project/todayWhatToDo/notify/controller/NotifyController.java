package com.project.todayWhatToDo.notify.controller;

import com.project.todayWhatToDo.common.response.PageContent;
import com.project.todayWhatToDo.common.response.RootResponse;
import com.project.todayWhatToDo.notify.dto.GetNotifyRequestDto;
import com.project.todayWhatToDo.notify.dto.NotifyDto;
import com.project.todayWhatToDo.notify.service.NotifyService;
import com.project.todayWhatToDo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;
    private final JwtService jwtService;

    @GetMapping("/list")
    public ResponseEntity<RootResponse> getAlarmList(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        var jwt = jwtService.getUserInfo(token);
        Page<NotifyDto> result = notifyService.getNotifies(new GetNotifyRequestDto(jwt.userId()), pageable);
        return ResponseEntity.ok(new RootResponse("success", PageContent.of(result)));
    }
}
