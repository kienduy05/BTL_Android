package com.example.quizapp.Models;

public class Question {
    private int questionId;
    private int practiceId;
    private String content;
    private String imageUrl;
    private String explanation;
    private Integer questionOrder;

    public Question() {
    }

    public Question(int questionId, int practiceId, String content, String imageUrl,
                    String explanation, Integer questionOrder) {
        this.questionId = questionId;
        this.practiceId = practiceId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.explanation = explanation;
        this.questionOrder = questionOrder;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(int practiceId) {
        this.practiceId = practiceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Integer getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(Integer questionOrder) {
        this.questionOrder = questionOrder;
    }
}