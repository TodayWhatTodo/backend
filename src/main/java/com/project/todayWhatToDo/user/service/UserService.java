package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.security.Authority;
import com.project.todayWhatToDo.security.UserSecurityInfo;
import com.project.todayWhatToDo.user.domain.Career;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.*;
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

import static com.project.todayWhatToDo.security.Authority.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginApiManager loginManager;

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

    public UserSession login(LoginRequestDto request) {

        var response = getUserInfo(request.provider(), request.token());

        var email = response.getEmail();
        var name = response.getName();
        var password = response.getPassword();

        User user = userRepository.findByEmailAndNameAndPassword(email, name, password)
                .orElseThrow(UserNotFoundException::new);

        return user.toSession();
    }

    private LoginResponseHandler getUserInfo(String provider, String token) {
        return loginManager.getProvider(provider)
                .getUserInfo(token);
    }

    public UserSession joinUser(CreateUserRequestDto request) {
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
                        .build()
                )
                .toSession();
    }

    public void modifyUserInfo(ModifyUserRequestDto request) {

        User user = userRepository.findById(request.id())
                .orElseThrow(UserNotFoundException::new);

        user.setNickname(request.nickname());
        user.setIntroduction(request.introduction());
        user.setCompanyName(request.companyName());
    }

    public void createCareer(CreateCareerRequestDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(UserNotFoundException::new);

        user.addCareer(
                Career.builder()
                        .user(user)
                        .name(request.name())
                        .introduction(request.introduction())
                        .startedAt(request.startedAt())
                        .endedAt(request.endedAt())
                        .position(request.position())
                        .build()
        );
    }
}
