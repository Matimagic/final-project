package com.finalproject.millionairesapplication.controller;

import com.finalproject.millionairesapplication.entity.User;
import com.finalproject.millionairesapplication.repositories.CategoryRepository;
import com.finalproject.millionairesapplication.repositories.UserRepository;
import com.finalproject.millionairesapplication.service.AppUserService;
import com.finalproject.millionairesapplication.service.QuizGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class UserController {

    private final AppUserService userService;
    private final UserRepository userRepository;
    private final QuizGameService quizGameService;
    private final CategoryRepository categoryRepository;

    @PostMapping("/register")
    public String addNewUser (
            @RequestParam String login,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password, Model model) {
        User user = new User();
        user.setLogin(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        userService.addNewUser(user);
        model.addAttribute("user", userService.loadUserByUsername(user.getLogin()));
        return "redirect:/user-home";
    }

    @GetMapping("/register")
    public String findUser () {
            return "register";
    }

    @GetMapping("/login")
    public String logUser () {
        return "login";
    }

    @GetMapping("/user-home")
    public String goToUserHome (Principal principal, Model model) {

        if(principal != null) {
            User user = userRepository.findByLoginIgnoringCase(principal.getName()).get();

            long correct =  quizGameService.getCorrectAnswers(user.getId());
            long incorrect =  quizGameService.getIncorrectAnswers(user.getId());
            long numberOfGames = quizGameService.getNumberOfGames(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("correctAnswers", correct);
            model.addAttribute("incorrectAnswers", incorrect);
            model.addAttribute("biggestWinning", quizGameService.getBiggestWinning(user.getId()));
            model.addAttribute("numberOfGames", numberOfGames);
            model.addAttribute("accuracy", quizGameService.getAccuracy(user.getId(), correct, incorrect));
            model.addAttribute("loggedInUser", principal.getName());

            return "user-home";
        }
        return "redirect:/login";
    }

}
