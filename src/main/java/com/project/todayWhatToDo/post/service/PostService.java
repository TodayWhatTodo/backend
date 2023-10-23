package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Keyword;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.PostRequestDto;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;

    public Long save(PostRequestDto requestDto) {
        Post savedPost = postRepository.save(requestDto.toEntity());
        Optional.ofNullable(requestDto.keywordList())
                .ifPresent(keywordRequestDtos -> keywordRequestDtos
                        .forEach(keywordRequestDto -> savedPost.addKeyword(Keyword.from(keywordRequestDto))));
        return savedPost.getId();
    }

    public void update(PostRequestDto requestDto) {
        postRepository.findFetchById(requestDto.id())
                .orElseThrow(PostNotFoundException::new)
                .update(requestDto);
    }

    public void delete(PostRequestDto requestDto) {
        postRepository.deleteById(requestDto.id());
    }

    public Post findFetchById(Long postId) {
       return postRepository.findFetchById(postId).orElseThrow(PostNotFoundException::new);
    }
}
