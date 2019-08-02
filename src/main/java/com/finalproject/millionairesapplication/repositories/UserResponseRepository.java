package com.finalproject.millionairesapplication.repositories;

import com.finalproject.millionairesapplication.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResponseRepository extends JpaRepository<UserResponse, Long> {
}
