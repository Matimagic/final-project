package com.finalproject.millionairesapplication.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Size(min = 3, max = 255)
    @Column(unique = true)
    private String login;

    @Email
    private String email;

    @Size(min = 3, max = 255)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<QuizGame> quizGameList;


}
