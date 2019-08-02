package com.finalproject.millionairesapplication.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class QuizGameDto {

    private UUID id;

    private List<QuizQuestion> questionList = new ArrayList<>();

    private User user;

    private List<Long> answers;

}
