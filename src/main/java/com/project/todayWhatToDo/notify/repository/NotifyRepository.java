package com.project.todayWhatToDo.notify.repository;

import com.project.todayWhatToDo.notify.domain.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
}
