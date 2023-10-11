package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.security.UserSecurityInfo;
import com.project.todayWhatToDo.user.domain.Career;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.CreateCareerRequestDto;
import com.project.todayWhatToDo.user.dto.FollowRequestDto;
import com.project.todayWhatToDo.user.dto.LoginRequestDto;
import com.project.todayWhatToDo.user.dto.ModifyUserRequestDto;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.login.LoginApiManager;
import com.project.todayWhatToDo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.project.todayWhatToDo.security.Authority.COMMON;

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

    public void login(LoginRequestDto request) {

        var response = loginManager.getProvider(request.oauthProvider())
                .getUserInfo(request.token());

        var email = response.getEmail();
        var name = response.getName();
        var password = response.getPassword();

        Optional<User> data = userRepository.findByEmailAndNameAndPassword(email, name, password);

        if (data.isEmpty()) {
            joinNormalUser(email, name, password);
        }
    }

    private void joinNormalUser(String email, String name, String password) {
        userRepository.save(User.builder()
                .authority(COMMON)
                .name(name)
                .nickname(UUID.randomUUID().toString())
                .password(password)
                .email(email)
                .build());
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

    public void follow(FollowRequestDto request){
        var follower = userRepository.findById(request.followerId()).orElseThrow(UserNotFoundException::new);
        var user = userRepository.findById(request.userId()).orElseThrow(UserNotFoundException::new);

        user.addFollowing(follower);
    }

    public void followCancel(){
    }
}
