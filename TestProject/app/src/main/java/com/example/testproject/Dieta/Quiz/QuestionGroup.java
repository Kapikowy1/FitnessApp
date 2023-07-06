package com.example.testproject.Dieta.Quiz;

import com.example.testproject.Dieta.Quiz.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionGroup {
    private final List<Question> questions;
    private int currentIndex;

    public QuestionGroup() {
        questions = new ArrayList<>();
        currentIndex = -1;
    }
    public void addQuestion(Question question) {
        questions.add(question);
    }
    public Question getNextQuestion() {
        if (questions.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex + 1) % questions.size();
        return questions.get(currentIndex);
    }
    public Question getCurrentQuestion() {
        if (questions.isEmpty()) {
            return null;
        }
        return questions.get(currentIndex);
    }

}