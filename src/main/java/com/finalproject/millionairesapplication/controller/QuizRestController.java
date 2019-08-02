package com.finalproject.millionairesapplication.controller;

import com.finalproject.millionairesapplication.entity.*;
import com.finalproject.millionairesapplication.service.DataUpdater;
import com.finalproject.millionairesapplication.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rest-api")
public class QuizRestController {

    private final DataUpdater dataUpdater;
    private final QuizService quizService;

    @PostMapping
    public QuizQuestion newQuestion(QuizQuestion question) {

        return quizService.newQuestion(question);
    }

    @GetMapping("/loadAll")
    public List<QuizQuestion> getAllQuestion(
            @RequestParam(required = false) List<Category> categoryList, QuestionValue questionValue) {

        if (categoryList != null && categoryList.size() >= 1) {
            return quizService.fetchQuestionsByCategoryAndValue(categoryList, questionValue);
        } else {
            return quizService.fetchQuestionsByValue(questionValue);
        }
    }

//    @GetMapping("/{id}")
//    public QuizQuestion getQuestionById(
//            @PathVariable Long id) {
//        return quizService.fetchQuestion(id);
//    }

    @GetMapping("/raffle-all")
    public void loadAll() {
        for (APICategories cat : APICategories.values()) {
            for (QuestionValue difficulty : QuestionValue.values()) {
                loadSingleCategory(cat.getApiCategoryId(), 30, difficulty.getLabel());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    private void loadSingleCategory(long apiCategoryId, int amount, String label) {
        String urlApi = "https://opentdb.com/api.php?amount=10&category={cat}&difficulty={difficulty_placeholder}&type=multiple"
                .replace("{cat}", String.valueOf(apiCategoryId))
                .replace("{difficulty_placeholder}", label);

        for (int i = 0; i < amount / 10; i++) {
            try {
                dataUpdater.saveQuestionFromApi(urlApi);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger(getClass().getName()).info("We were blocked!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                }

                Logger.getLogger(getClass().getName()).info("Waking up and returning to work!");
            }
        }
    }

    @GetMapping("/raffle")
    public void getAnyQuestion(@RequestParam(name = "category", required = false) Integer category,
                               @RequestParam(name = "amount", required = false) Integer amount,
                               @RequestParam(name = "difficulty", required = false) String difficulty) {

        amount = amount == null ? 1 : amount;
        category = category == null || category < 9 || category > 32 ? 9 : category;

        difficulty = checkDifficulty(difficulty);

        String urlApi = "https://opentdb.com/api.php?amount=1&category={cat}&difficulty={difficulty_placeholder}&type=multiple"
                .replace("{cat}", category.toString())
                .replace("{difficulty_placeholder}", difficulty);

        for (int i = 0; i < amount; i++) {
            try {
                dataUpdater.saveQuestionFromApi(urlApi);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("we did it!");
    }

    public static String checkDifficulty(String difficulty) {
        if (difficulty == null) {
            return "easy";
        }
        if (difficulty.isEmpty() || (!difficulty.equals("easy") && !difficulty.equals("medium") && !difficulty.equals("hard"))) {
            return "easy";
        }
        return difficulty;
    }
}
