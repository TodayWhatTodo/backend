package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.PostRequestDto;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final KeywordInfoService keywordInfoService;

    public Long save(PostRequestDto requestDto) {
        Post savedPost = postRepository.save(requestDto.toEntity());
        keywordInfoService.saveKeyword(savedPost, requestDto.keywordList());
        return savedPost.getId();
    }

    public void update(PostRequestDto requestDto) {
        postRepository.findById(requestDto.id())
                .orElseThrow(PostNotFoundException::new)
                .update(requestDto);
    }

    public void delete(PostRequestDto requestDto) {
        postRepository.deleteById(requestDto.id());
    }

    public Post findById(Long postId) {
       return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }
}
