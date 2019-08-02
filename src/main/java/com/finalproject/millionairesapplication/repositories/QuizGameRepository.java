package com.finalproject.millionairesapplication.repositories;

import com.finalproject.millionairesapplication.entity.QuizGame;
import com.finalproject.millionairesapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizGameRepository extends JpaRepository<QuizGame, String> {

    List<QuizGame> findAllByUser (User user);

}
