package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.dto.request.PostRequestDto;
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

    public void save(PostRequestDto requestDto) {
        postRepository.save(requestDto.toEntity());
    }

    public void update(PostRequestDto requestDto) {
        postRepository.findById(requestDto.getId())
                .orElseThrow(PostNotFoundException::new)
                .update(requestDto);
    }

    public void delete(PostRequestDto requestDto) {
        postRepository.deleteById(requestDto.getId());
    }

}
