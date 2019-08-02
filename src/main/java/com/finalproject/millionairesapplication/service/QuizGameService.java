package com.finalproject.millionairesapplication.service;

import com.finalproject.millionairesapplication.entity.*;
import com.finalproject.millionairesapplication.repositories.AnswerRepository;
import com.finalproject.millionairesapplication.repositories.QuizGameRepository;
import com.finalproject.millionairesapplication.repositories.UserRepository;
import com.finalproject.millionairesapplication.repositories.UserResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizGameService {

    private final QuizGameRepository quizGameRepository;
    private final UserResponseRepository userResponseRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public void saveQuizGameWithUser(QuizGame quizGame, String username) {
        userResponseRepository.saveAll(quizGame.getQuestionList());

        User u = userRepository.findByLoginIgnoringCase(username).orElse(null);

        quizGame.setUser(u);
        quizGameRepository.save(quizGame);

        if (u != null) {
            u.getQuizGameList().add(quizGame);
            userRepository.save(u);
        }
    }

    public Optional<QuizQuestion> submitAnswerAndPollNextQuestion(String gameId, Long answerId, Long questionNumber) {
        QuizGame game = quizGameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("no game found!"));

        UserResponse response = game.getQuestionList().get(Math.toIntExact(questionNumber));
        response.setAnswerId(answerId);
        userResponseRepository.save(response);
        quizGameRepository.save(game);

        for (int i = 0; i < game.getQuestionList().size(); i++) {
            if (game.getQuestionList().get(i).getAnswerId() == null) {
                return Optional.of(game.getQuestionList().get(i).getQuizQuestion());
            }
        }
        return Optional.empty();
    }

    public boolean isAnswerCorrect(Long answerId) {
        Answer a = answerRepository.findById(answerId).orElseThrow(() -> new RuntimeException("no answer found!"));
        return a.getIsCorrect();
    }

    public int getBiggestWinning(Long userId) {
        List<QuizGame> quizGameList = quizGameRepository.findAllByUser(userRepository.getOne(userId));
        Optional<QuizGame> bestGame = quizGameList.stream()
                .max(Comparator.comparingLong(
                        game -> game.getQuestionList()
                                .stream()
                                .filter(quizQuestion -> quizQuestion.getAnswerId() != null)
                                .filter(userResponse -> answerRepository.isCorrectAnswer(userResponse.getAnswerId()))
                                .mapToInt(userResponse -> 1)
                                .sum()));
        if (bestGame.isPresent()) {
            QuizGame game = bestGame.get();
            return evaluateWinning(game.getQuestionList()
                    .stream()
                    .filter(quizQuestion -> quizQuestion.getAnswerId() != null)
                    .filter(userResponse -> answerRepository.isCorrectAnswer(userResponse.getAnswerId()))
                    .mapToInt(userResponse -> 1)
                    .sum());
        }
        return 0;
    }

    public long getCorrectAnswers(Long userId) {
        List<QuizGame> quizGameList = quizGameRepository.findAllByUser(userRepository.getOne(userId));
        long correctAnswers = quizGameList.stream()
                .flatMap(game -> game.getQuestionList().stream())
                .filter(quizQuestion -> quizQuestion.getAnswerId() != null)
                .filter(quizQuestion -> answerRepository.isCorrectAnswer(quizQuestion.getAnswerId()))
                .count();
        return correctAnswers;
    }

    public long getIncorrectAnswers(Long userId) {
        List<QuizGame> quizGameList = quizGameRepository.findAllByUser(userRepository.getOne(userId));
        long incorrectAnswers = quizGameList.stream()
                .flatMap(game -> game.getQuestionList().stream())
                .filter(quizQuestion -> quizQuestion.getAnswerId() != null)
                .filter(quizQuestion -> !answerRepository.isCorrectAnswer(quizQuestion.getAnswerId()))
                .count();
        return incorrectAnswers;
    }

    public int returnValueForQuestion(String quizGameId, boolean next) {
        QuizGame game = quizGameRepository.findById(quizGameId).orElseThrow(() -> new RuntimeException("no game found"));

        int i;
        for (i = 0; i < game.getQuestionList().size(); i++) {
            if (game.getQuestionList().get(i).getAnswerId() == null) {
                break;
            }
        }

        if (i==15) {
            if (answerRepository.findById(game.getQuestionList().get(i-1).getAnswerId()).get().getIsCorrect()){
                return evaluateWinning(i);
            }
        }
        return evaluateWinning(i + (next ? 1 : -1));
    }

    private int evaluateWinning(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 100;
            case 2:
                return 200;
            case 3:
                return 300;
            case 4:
                return 500;
            case 5:
                return 1000;
            case 6:
                return 2000;
            case 7:
                return 4000;
            case 8:
                return 8000;
            case 9:
                return 16000;
            case 10:
                return 32000;
            case 11:
                return 64000;
            case 12:
                return 125000;
            case 13:
                return 250000;
            case 14:
                return 500000;
            case 15:
                return 1000000;
        }
        return 0;
    }

    public int getNumberOfGames(Long id) {
        return quizGameRepository.findAllByUser(userRepository.getOne(id)).size();
    }

    public double getAccuracy(Long id, long correct, long incorrect) {
        if (incorrect == 0) {
            return 100.00;
        }
        return (((double) correct / ((double) (incorrect + correct)) * 100));

    }

}

