package com.finalproject.millionairesapplication.repositories;

import com.finalproject.millionairesapplication.entity.Category;
import com.finalproject.millionairesapplication.entity.QuestionValue;
import com.finalproject.millionairesapplication.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findQuizQuestionsByCategoryInAndQuestionValue(List<Category> categoryList, QuestionValue questionValue);

    List<QuizQuestion> findQuizQuestionsByQuestionValue(QuestionValue questionValue);

    List<QuizQuestion> findAllByDescriptionAndCategory(String description, Category category);

}
