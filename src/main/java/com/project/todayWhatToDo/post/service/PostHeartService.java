package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Heart;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.CreateHeartRequest;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class PostHeartService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post heartPost(CreateHeartRequest requestDto) {
        Post post = postRepository.findById(requestDto.postId()).orElseThrow(PostNotFoundException::new);
        post.getHeartList().stream()
                .map(Heart::getUser)
                .filter(user -> Objects.equals(user.getId(), requestDto.userId()))
                .findAny()
                .ifPresentOrElse(
                        post::removeHeart
                        , () -> post.addHeart(userRepository.findById(requestDto.userId()).orElseThrow(UserNotFoundException::new)));

        return post;
    }
}
