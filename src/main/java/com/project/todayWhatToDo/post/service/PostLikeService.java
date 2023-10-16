package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Like;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.request.PostLikeRequestDto;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.PostLikeRepository;
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
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    public void likePost(PostLikeRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.postId()).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findById(requestDto.userId()).orElseThrow(UserNotFoundException::new);
        Optional<Like> likePost = postLikeRepository.findByPostIdAndUserId(post.getId(), user.getId());

        if (likePost.isPresent()) {
            post.decreaseLike();
            postLikeRepository.delete(likePost.get());
        } else {
            post.addLike(user);
        }
    }
}
