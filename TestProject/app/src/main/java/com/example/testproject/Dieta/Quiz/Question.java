package com.example.testproject.Dieta.Quiz;

public class Question {
    private final String questionText;
    private final String answer;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;


    public Question(String questionText, String answer,String optionA,String optionB,String optionC,String optionD) {
        this.optionA=optionA;
        this.optionB=optionB;
        this.optionC=optionC;
        this.optionD=optionD;
        this.questionText = questionText;
        this.answer = answer;
    }

    public String getQuestionText() {
        return questionText;
    }
    public String getOptionA() {
        return optionA;
    }
    public String getOptionB() {
        return optionB;
    }
    public String getOptionC() {
        return optionC;
    }
    public String getOptionD() {
        return optionD;
    }
    public String getCorrectAnswer() {
        return answer;
    }




}
