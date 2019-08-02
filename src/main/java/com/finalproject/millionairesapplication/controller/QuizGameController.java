package com.finalproject.millionairesapplication.controller;

import com.finalproject.millionairesapplication.entity.QuizQuestion;
import com.finalproject.millionairesapplication.service.QuizGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/quizgame/")
@RequiredArgsConstructor
public class QuizGameController {

    private final QuizGameService quizGameService;

    @GetMapping("/{game_identifier}/stats")
    public String loadEndGameScreen(@PathVariable(name = "game_identifier") String gameId,
                                    Model model) {

        model.addAttribute("earning", quizGameService.returnValueForQuestion(gameId, false));

        model.addAttribute("game_identifier", gameId);
        model.addAttribute("gameEnded", true);

        return "end-game";
    }

    @PostMapping("/play/{game_identifier}/{question_number}")
    public String loadNextQuestion(
            @PathVariable(name = "game_identifier") String gameId,
            @PathVariable(name = "question_number") Long questionNumber,
            @RequestParam(name = "answer") Long answerId,
            Model model) {

        Optional<QuizQuestion> q = quizGameService.submitAnswerAndPollNextQuestion(gameId, answerId, questionNumber);

        if (questionNumber==14 || !quizGameService.isAnswerCorrect(answerId)) {
            model.addAttribute("game_identifier", gameId);
            return "redirect:/quizgame/" + gameId + "/stats";
        }
            if (q.isPresent()) {
                model.addAttribute("game_identifier", gameId);
                model.addAttribute("question_number", ++questionNumber);
                model.addAttribute("question", q.get());
                model.addAttribute("questionValue", quizGameService.returnValueForQuestion(gameId, true));

                return "basic-quiz";
            }
        return null;
    }
}
