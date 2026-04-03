package com.example.quizapp.Models;

public class Practice {
    private int practiceId;
    private int categoryId;
    private String practiceName;
    private String practiceDescription;
    private Integer timeLimit;
    private Integer totalQuestions;
    private Integer createdBy;
    private String createdAt;
    private String updatedAt;

    public Practice() {
    }

    public Practice(int practiceId, int categoryId, String practiceName, String practiceDescription,
                    Integer timeLimit, Integer totalQuestions, Integer createdBy,
                    String createdAt, String updatedAt) {
        this.practiceId = practiceId;
        this.categoryId = categoryId;
        this.practiceName = practiceName;
        this.practiceDescription = practiceDescription;
        this.timeLimit = timeLimit;
        this.totalQuestions = totalQuestions;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(int practiceId) {
        this.practiceId = practiceId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    public String getPracticeDescription() {
        return practiceDescription;
    }

    public void setPracticeDescription(String practiceDescription) {
        this.practiceDescription = practiceDescription;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}