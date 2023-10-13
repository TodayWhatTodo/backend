package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.LikePost;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.PostLikeRepository;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.user.domain.User;
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

    public void likePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<LikePost> likePost = postLikeRepository.findByPostIdAndUserId(post.getId(), user.getId());

        if (likePost.isPresent()) {
            postLikeRepository.delete(likePost.get());
        } else {
            postLikeRepository.save(LikePost.of(user, post));
        }
    }
}
