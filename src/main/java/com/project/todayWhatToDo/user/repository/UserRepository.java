package com.project.todayWhatToDo.user.repository;

import com.project.todayWhatToDo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
