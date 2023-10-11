package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.user.dto.UpdateUserSettingRequestDto;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class SettingService {
    private final UserRepository userRepository;

    public void setting(UpdateUserSettingRequestDto request, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new)
                .setting(request);
    }
}