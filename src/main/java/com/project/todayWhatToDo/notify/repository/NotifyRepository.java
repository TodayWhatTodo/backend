package com.project.todayWhatToDo.notify.repository;

import com.project.todayWhatToDo.notify.domain.Notify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
    Page<Notify> findByUserId(Long aLong, Pageable pageable);

    void deleteAllByIdInAndUserId(List<Long> longs, Long aLong);
}
