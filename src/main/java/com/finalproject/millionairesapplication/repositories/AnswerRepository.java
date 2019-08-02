package com.finalproject.millionairesapplication.repositories;

import com.finalproject.millionairesapplication.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(nativeQuery = true, value = "select a.iscorrect from `answer` a where a.id= ?1")
    boolean isCorrectAnswer(Long id);

}
