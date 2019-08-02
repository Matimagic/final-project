package com.finalproject.millionairesapplication.service;

import com.finalproject.millionairesapplication.entity.Category;
import com.finalproject.millionairesapplication.entity.QuestionValue;
import com.finalproject.millionairesapplication.entity.QuizQuestion;
import com.finalproject.millionairesapplication.repositories.CategoryRepository;
import com.finalproject.millionairesapplication.repositories.QuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final CategoryRepository categoryRepository;

    public QuizQuestion newQuestion(QuizQuestion quizQuestion) {
        return quizQuestionRepository.save(quizQuestion);
    }

    public QuizQuestion fetchQuestion(Long id) {
        return quizQuestionRepository.findById(id).
                orElseThrow(() -> new RuntimeException("question not found in database"));
    }

    public List<QuizQuestion> fetchQuestionsByCategoryAndValue(List<Category> categoryList, QuestionValue questionValue) {
        return quizQuestionRepository.findQuizQuestionsByCategoryInAndQuestionValue(categoryList, questionValue);
    }

    public List<QuizQuestion> fetchQuestionsByValue(QuestionValue questionValue) {
        return quizQuestionRepository.findQuizQuestionsByQuestionValue(questionValue);
    }

    public List<QuizQuestion> randomizeQuestionWithCategory (
            String... categories) {
        List<QuizQuestion> quizQuestionList = new ArrayList<>();
        Random generator = new Random();
        List<Category> categoryList = new ArrayList<>();

        for (String cat :
                categories) {
            if(categoryRepository.findByName(cat).isPresent()) {
                categoryList.add(categoryRepository.findByName(cat).get());
            }
        }

        if (categoryList.size()==0) {
            categoryList.addAll(categoryRepository.findAll());
        }

        List<QuizQuestion> easyQuests = quizQuestionRepository.findQuizQuestionsByCategoryInAndQuestionValue(categoryList, QuestionValue.EASY);
        while (quizQuestionList.size() < 5) {
            QuizQuestion q = easyQuests.get(generator.nextInt(easyQuests.size()));
            if (!quizQuestionList.contains(q)) {
                quizQuestionList.add(q);
            }
        }

        List<QuizQuestion> mediumQuests = quizQuestionRepository.findQuizQuestionsByCategoryInAndQuestionValue(categoryList, QuestionValue.MEDIUM);
        while (quizQuestionList.size()< 10) {
            QuizQuestion q = mediumQuests.get(generator.nextInt(mediumQuests.size()));
            if (!quizQuestionList.contains(q)) {
                quizQuestionList.add(q);
            }
        }

        List<QuizQuestion> hardQuests = quizQuestionRepository.findQuizQuestionsByCategoryInAndQuestionValue(categoryList, QuestionValue.HARD);
        while (quizQuestionList.size()< 15) {
            QuizQuestion q = hardQuests.get(generator.nextInt(hardQuests.size()));
            if (!quizQuestionList.contains(q)) {
                quizQuestionList.add(q);
            }
        }

    return quizQuestionList;
    }

}
