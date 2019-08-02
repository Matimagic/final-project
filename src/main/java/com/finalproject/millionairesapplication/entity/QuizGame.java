package com.finalproject.millionairesapplication.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_game")
@Data
public class QuizGame {

    @Id
    private String id;

    @OneToMany
    private List<UserResponse> questionList = new ArrayList<>();

    @ManyToOne
    private User user;

}
