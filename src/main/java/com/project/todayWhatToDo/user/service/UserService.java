package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.security.UserSecurityInfo;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.ModifyUserRequest;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.login.LoginApiManager;
import com.project.todayWhatToDo.user.repository.UserRepository;
import com.project.todayWhatToDo.user.dto.LoginRequestDto;
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

        var response = loginManager.getProvider(request.getOauthProvider())
                .getUserInfo(request.getToken());

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

    public void modifyUserInfo(ModifyUserRequest request) {
        Optional<User> user = userRepository.findById(request.id());
        user.orElseThrow(UserNotFoundException::new);

        User entity = user.get();
        if (request.nickname() != null) entity.setNickname(request.nickname());
    }
}
