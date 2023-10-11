package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.user.dto.UpdateCareerRequestDto;
import com.project.todayWhatToDo.user.exception.CareerNotFoundException;
import com.project.todayWhatToDo.user.repository.CareerRepository;
import com.project.todayWhatToDo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CareerService {

    private final UserRepository userRepository;
    private final CareerRepository careerRepository;

    public void updateCareer(UpdateCareerRequestDto request, Long userId) {
        careerRepository.findByIdAndUserId(request.careerId(), userId)
                .orElseThrow(CareerNotFoundException::new)
                .update(request);
    }
}
