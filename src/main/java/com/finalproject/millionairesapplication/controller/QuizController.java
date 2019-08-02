package com.finalproject.millionairesapplication.controller;

import com.finalproject.millionairesapplication.entity.*;
import com.finalproject.millionairesapplication.service.QuizGameService;
import com.finalproject.millionairesapplication.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app-home/")
public class QuizController {

    private final QuizService quizService;
    private final QuizGameService quizGameService;

    @GetMapping
    public String loadHome(Model model, Principal principal) {
        model.addAttribute("questions", Arrays.asList("a", "b"));
        model.addAttribute("loggedInUser", principal == null ? "none" : principal.getName());

        return "app-home";
    }

    @GetMapping("/play")
    public String loadPageWithQuiz(
            @RequestParam Map<String, String> params,
            Principal principal,
            Model model) {

        List<String> selectedCategories = new ArrayList<>(params.values());

        UUID identifier = UUID.randomUUID();
        QuizGame quizGame = new QuizGame();
        quizGame.setQuestionList(
                quizService.randomizeQuestionWithCategory(selectedCategories.toArray(new String[selectedCategories.size()]))
                        .stream()
                        .map(quizQuestion -> new UserResponse(null, quizQuestion, null))
                        .collect(Collectors.toList()));

        quizGame.setId(identifier.toString());

        quizGameService.saveQuizGameWithUser(quizGame, principal != null ? principal.getName() : null);

        model.addAttribute("game_identifier", identifier.toString());
        model.addAttribute("question_number", 0);
        model.addAttribute("question", quizGame.getQuestionList().get(0).getQuizQuestion());
        model.addAttribute("questionValue", quizGameService.returnValueForQuestion(quizGame.getId(), true));

        return "basic-quiz";
    }

}
