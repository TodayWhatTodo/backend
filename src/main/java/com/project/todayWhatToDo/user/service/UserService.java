package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.security.UserSecurityInfo;
import com.project.todayWhatToDo.user.domain.Job;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.*;
import com.project.todayWhatToDo.user.dto.request.CreateUserRequest;
import com.project.todayWhatToDo.user.dto.request.LoginRequest;
import com.project.todayWhatToDo.user.dto.request.ModifyUserRequest;
import com.project.todayWhatToDo.user.dto.response.ProfileResponse;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.login.LoginApiManager;
import com.project.todayWhatToDo.user.login.handler.LoginResponseHandler;
import com.project.todayWhatToDo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static com.project.todayWhatToDo.security.Authority.COMMON;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginApiManager loginManager;
    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException(name + " 이름을 가진 유저는 존재하지 않습니다."));

        return UserSecurityInfo.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .authority(user.getAuthority().name())
                .build();
    }

    public UserSession login(LoginRequest request) {

        var response = getUserInfo(request.provider(), request.token());

        var email = response.getEmail();
        var name = response.getName();
        var password = response.getPassword();

        return userRepository.findByEmailAndNameAndPassword(email, name, password)
                .orElseThrow(UserNotFoundException::new)
                .toSession();
    }

    private LoginResponseHandler getUserInfo(String provider, String token) {
        return loginManager.getProvider(provider)
                .getUserInfo(restTemplate, token);
    }

    public UserSession joinUser(CreateUserRequest request) {
        var response = getUserInfo(request.provider(), request.token());

        var name = response.getName();
        var email = response.getEmail();
        var password = response.getPassword();

        return userRepository.save(User.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .introduction(request.introduction())
                        .isAcceptAlarm(request.isAcceptAlarm())
                        .imagePath(request.imagePath())
                        .authority(COMMON)
                        .build())
                .toSession();
    }

    public void modifyUserInfo(ModifyUserRequest request) {

        User user = userRepository.findById(request.id())
                .orElseThrow(UserNotFoundException::new);

        user.setNickname(request.nickname());
        user.setIntroduction(request.introduction());
        user.setJob(Job.builder()
                .companyName(request.companyName())
                .address(request.companyAddress())
                .build());
        user.setImagePath(request.imagePath());

    }

    public ProfileResponse getProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new)
                .toProfile();
    }

    public void quitUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new)
                .quit();
    }
}