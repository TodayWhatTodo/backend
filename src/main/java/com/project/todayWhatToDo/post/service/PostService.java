package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.Keyword;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.KeywordRequestDto;
import com.project.todayWhatToDo.post.dto.PostRequestDto;
import com.project.todayWhatToDo.post.exception.KeywordNotFoundException;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.KeywordRepository;
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
    private final KeywordRepository keywordRepository;

    public Long save(PostRequestDto requestDto) {
        Post savedPost = postRepository.save(requestDto.toEntity());
        Optional.ofNullable(requestDto.keywordList())
                .ifPresent(keywordRequestDtos -> keywordRequestDtos
                        .forEach(keywordRequestDto -> savedPost.addKeyword(Keyword.from(keywordRequestDto))));
        return savedPost.getId();
    }

    public Long update(PostRequestDto requestDto) {
        Post post = postRepository.findFetchById(requestDto.id())
                .orElseThrow(PostNotFoundException::new);

        post.update(requestDto);

        requestDto.keywordList().stream()
                .filter(this::check)
                .forEach(target ->
                    keywordRepository.findByKeywordAndPostId(target.keyword(), post.getId())
                            .orElseThrow(KeywordNotFoundException::new).update(target.after())
                );
        return post.getId();
    }

    public void delete(PostRequestDto requestDto) {
        postRepository.deleteById(requestDto.id());
    }

    protected Post findFetchById(Long postId) {
       return postRepository.findFetchById(postId).orElseThrow(PostNotFoundException::new);
    }

    private boolean check(KeywordRequestDto keywordRequestDto) {
        return !keywordRequestDto.after().isBlank();
    }
}
