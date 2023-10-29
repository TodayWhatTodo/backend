package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Heart;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.PostHeartRequestDto;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.PostHeartRepository;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostHeartService {

    private final PostRepository postRepository;
    private final PostHeartRepository postHeartRepository;
    private final UserRepository userRepository;

    public void likePost(PostHeartRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.postId()).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findById(requestDto.userId()).orElseThrow(UserNotFoundException::new);
        Optional<Heart> likePost = postHeartRepository.findByPostIdAndUserId(post.getId(), user.getId());

        if (likePost.isPresent()) {
            post.decreaseLike();
            postHeartRepository.delete(likePost.get());
        } else {
            post.addLike(user);
        }
    }
}
