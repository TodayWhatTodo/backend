package com.project.todayWhatToDo.post.repository;

import com.project.todayWhatToDo.post.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<AttachFile, Long> {
}
