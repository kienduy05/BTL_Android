package com.example.quizapp.Models;

public class ResultDetail {
    private int resultDetailId;
    private int resultId;
    private int questionId;
    private Integer selectedAnswerOptionId;
    private int isCorrect;

    public ResultDetail() {
    }

    public ResultDetail(int resultDetailId, int resultId, int questionId, Integer selectedAnswerOptionId, int isCorrect) {
        this.resultDetailId = resultDetailId;
        this.resultId = resultId;
        this.questionId = questionId;
        this.selectedAnswerOptionId = selectedAnswerOptionId;
        this.isCorrect = isCorrect;
    }

    public int getResultDetailId() {
        return resultDetailId;
    }

    public void setResultDetailId(int resultDetailId) {
        this.resultDetailId = resultDetailId;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public Integer getSelectedAnswerOptionId() {
        return selectedAnswerOptionId;
    }

    public void setSelectedAnswerOptionId(Integer selectedAnswerOptionId) {
        this.selectedAnswerOptionId = selectedAnswerOptionId;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }
}