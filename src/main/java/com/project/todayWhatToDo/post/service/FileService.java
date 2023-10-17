package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.post.dto.request.FileRequestDto;
import com.project.todayWhatToDo.post.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;


    public Long saveFile(FileRequestDto requestDto) {
        return fileRepository.save(requestDto.toEntity()).getId();
    }
}
