package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.CreatePostRequest;
import com.project.todayWhatToDo.post.dto.DeletePostRequest;
import com.project.todayWhatToDo.post.dto.UpdatePostRequest;
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

    public Long save(CreatePostRequest requestDto) {
        return postRepository.save(requestDto.toEntity()).getId();
    }

    public Long update(UpdatePostRequest requestDto) {
        Post post = postRepository.findFetchById(requestDto.id())
                .orElseThrow(PostNotFoundException::new);

        post.update(requestDto);
        return post.getId();
    }

    public void delete(DeletePostRequest requestDto) {
        postRepository.deleteById(requestDto.id());
    }

}
