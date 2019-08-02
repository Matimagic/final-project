package com.finalproject.millionairesapplication.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;


@Entity
@Getter
@Setter
@Table(name = "question")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Enumerated(EnumType.STRING)
    private QuestionValue questionValue;

    private String description;

    @OneToMany
    private List<Answer> answers = new ArrayList<>();

    private boolean defaultQuestion;

    private String language;

    public Set<Answer> randomizeAnswers () {
        return new HashSet<>(this.answers);

    }

}
