package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.user.domain.Career;
import com.project.todayWhatToDo.user.domain.Job;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.CreateCareerRequestDto;
import com.project.todayWhatToDo.user.dto.UpdateCareerRequestDto;
import com.project.todayWhatToDo.user.exception.CareerNotFoundException;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
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

    public void deleteCareer(Long careerId, Long userId) {
        careerRepository.deleteByIdAndUserId(careerId, userId);
    }

    public void createCareer(CreateCareerRequestDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(UserNotFoundException::new);

        user.addCareer(
                Career.builder()
                        .user(user)
                        .job(Job.builder()
                                .companyName(request.name())
                                .address(request.address())
                                .position(request.position())
                                .build()
                        )
                        .introduction(request.introduction())
                        .startedAt(request.startedAt())
                        .endedAt(request.endedAt())
                        .build()
        );
    }
}