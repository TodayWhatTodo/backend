package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.CreatePostRequestDto;
import com.project.todayWhatToDo.post.dto.DeletePostRequestDto;
import com.project.todayWhatToDo.post.dto.UpdatePostRequestDto;
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

    public Long save(CreatePostRequestDto requestDto) {
        return postRepository.save(requestDto.toEntity()).getId();
    }

    public Long update(UpdatePostRequestDto requestDto) {
        Post post = postRepository.findFetchById(requestDto.id())
                .orElseThrow(PostNotFoundException::new);

        post.update(requestDto);
        return post.getId();
    }

    public void delete(DeletePostRequestDto requestDto) {
        postRepository.deleteById(requestDto.id());
    }

    protected Post findFetchById(Long postId) {
       return postRepository.findFetchById(postId).orElseThrow(PostNotFoundException::new);
    }

}
