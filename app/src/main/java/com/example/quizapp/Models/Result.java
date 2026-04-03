package com.example.quizapp.Models;

public class Result {
    private int resultId;
    private int userId;
    private int practiceId;
    private Double score;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer totalQuestions;
    private Integer duration;
    private String submittedAt;

    public Result() {
    }

    public Result(int resultId, int userId, int practiceId, Double score, Integer correctCount,
                  Integer wrongCount, Integer totalQuestions, Integer duration, String submittedAt) {
        this.resultId = resultId;
        this.userId = userId;
        this.practiceId = practiceId;
        this.score = score;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.totalQuestions = totalQuestions;
        this.duration = duration;
        this.submittedAt = submittedAt;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(int practiceId) {
        this.practiceId = practiceId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(Integer wrongCount) {
        this.wrongCount = wrongCount;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }
}