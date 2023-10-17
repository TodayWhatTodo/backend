package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.domain.AttachFile;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.dto.request.FileRequestDto;
import com.project.todayWhatToDo.post.dto.request.PostRequestDto;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;

    @Value("${file.path}")
    private String path;


    public void save(PostRequestDto requestDto) throws Exception {
        Post savedPost = postRepository.save(requestDto.toEntity());
        List<MultipartFile> files = requestDto.files();
        if (!files.isEmpty()) {
            for (MultipartFile file: files) {
                AttachFile attachFile = saveFile(file);
                attachFile.setPost(savedPost);
            }
        }
    }

    public void update(PostRequestDto requestDto) {
        postRepository.findById(requestDto.id())
                .orElseThrow(PostNotFoundException::new)
                .update(requestDto);
    }

    public void delete(PostRequestDto requestDto) {
        postRepository.deleteById(requestDto.id());
    }



    private AttachFile saveFile(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String filename = new MD5Generator(Objects.requireNonNull(originalFilename)).toString();
        String fullPath = path + originalFilename;

        file.transferTo(new File(fullPath));

        return FileRequestDto.builder()
                .filename(filename)
                .filePath(fullPath)
                .origFilename(originalFilename)
                .build().toEntity();


//        fileService.saveFile(fileRequestDto);
    }

}
