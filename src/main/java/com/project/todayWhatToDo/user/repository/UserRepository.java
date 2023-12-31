package com.project.todayWhatToDo.user.repository;

import com.project.todayWhatToDo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndNameAndPassword(String email, String name, String password);
}
