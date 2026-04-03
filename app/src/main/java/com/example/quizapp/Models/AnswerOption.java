package com.example.quizapp.Models;

public class AnswerOption {
    private int answerOptionId;
    private int questionId;
    private String answerText;
    private int isCorrect;
    private Integer optionOrder;

    public AnswerOption() {
    }

    public AnswerOption(int answerOptionId, int questionId, String answerText, int isCorrect, Integer optionOrder) {
        this.answerOptionId = answerOptionId;
        this.questionId = questionId;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.optionOrder = optionOrder;
    }

    public int getAnswerOptionId() {
        return answerOptionId;
    }

    public void setAnswerOptionId(int answerOptionId) {
        this.answerOptionId = answerOptionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getOptionOrder() {
        return optionOrder;
    }

    public void setOptionOrder(Integer optionOrder) {
        this.optionOrder = optionOrder;
    }
}