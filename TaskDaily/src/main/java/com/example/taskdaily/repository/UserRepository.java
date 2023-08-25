package com.example.taskdaily.repository;

import com.example.taskdaily.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
