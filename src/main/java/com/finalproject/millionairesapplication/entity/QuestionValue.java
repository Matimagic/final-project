package com.finalproject.millionairesapplication.entity;

public enum QuestionValue {

    EASY(1, "easy"),
    MEDIUM(2, "medium"),
    HARD(3, "hard");

    private int value;
    private String label;

    QuestionValue(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
