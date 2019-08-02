package com.finalproject.millionairesapplication.service;

import com.finalproject.millionairesapplication.entity.*;
import com.finalproject.millionairesapplication.repositories.AnswerRepository;
import com.finalproject.millionairesapplication.repositories.CategoryRepository;
import com.finalproject.millionairesapplication.repositories.QuizQuestionRepository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataUpdater {

    private final QuizQuestionRepository quizQuestionRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;
    private final RestTemplate restTemplate;

    public void saveQuestionFromApi(String urlApi) throws Exception {
        QuestionJsonDto dto = restTemplate.getForObject(urlApi, QuestionJsonDto.class);
        assert dto != null;
        dto.getResults().forEach(this::parseResultIntoQuestionEntity);
    }

    private void parseResultIntoQuestionEntity(Result dtoResult) {
        QuizQuestion question = new QuizQuestion();
        String questionDescrpition = dtoResult.getQuestion();

        question.setDescription(setEncoding(questionDescrpition));

        Category category = extractAndSaveCategory(question, dtoResult);

        if (checkIfQuestionExists(question.getDescription(), category)) {
            return;
        }

        Answer correctAnswer = new Answer();
        correctAnswer.setDescription(dtoResult.getCorrect_answer());
        correctAnswer.setIsCorrect(true);

        question.getAnswers().add(correctAnswer);

        for (int i = 0; i < dtoResult.getIncorrect_answers().size(); i++) {
            Answer incorrect = new Answer();
            incorrect.setDescription(dtoResult.getIncorrect_answers().get(i));
            incorrect.setIsCorrect(false);

            question.getAnswers().add(incorrect);
        }

        question.setQuestionValue(QuestionValue.valueOf(dtoResult.getDifficulty().toUpperCase()));

        question.setDefaultQuestion(true);

        question.setLanguage("EN_us");

        answerRepository.saveAll(question.getAnswers());
        quizQuestionRepository.save(question);
    }

    private boolean checkIfQuestionExists(String description, Category category) {
        List<QuizQuestion> triedQuestions =
                quizQuestionRepository.findAllByDescriptionAndCategory(description, category);
        return !triedQuestions.isEmpty();
    }

    private Category extractAndSaveCategory(QuizQuestion question, Result dtoResult) {
        String categoryName = dtoResult.getCategory();

        Category category;
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if (!categoryOptional.isPresent()) {
            category = new Category();
            category.setName(categoryName);
            category = categoryRepository.save(category);

            question.setCategory(category);
        } else {
            category = categoryOptional.get();
            question.setCategory(category);
        }
        return category;
    }

    private String setEncoding(String input) {
        return StringEscapeUtils.unescapeHtml4(input);
    }

}
