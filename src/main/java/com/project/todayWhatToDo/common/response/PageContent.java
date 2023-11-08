package com.project.todayWhatToDo.common.response;

import com.project.todayWhatToDo.notify.dto.NotifyDto;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public record PageContent(
        int page,
        int size,
        long totalElements,
        int totalPages,
        Collection<?> contents
) {

    public static PageContent of(Page<?> page) {
        List<?> content = page.getContent();

        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        int index = page.getNumber();
        int size = page.getSize();

        return new PageContent(index, size, totalElements, totalPages, content);
    }
}
