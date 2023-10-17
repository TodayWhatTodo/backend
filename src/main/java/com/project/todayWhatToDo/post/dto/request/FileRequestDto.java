package com.project.todayWhatToDo.post.dto.request;

import com.project.todayWhatToDo.post.domain.AttachFile;
import lombok.Builder;

@Builder
public record FileRequestDto(
        Long id,
        String origFilename,
        String filename,
        String filePath
) {
    public AttachFile toEntity() {
        return AttachFile.builder()
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
    }
}
