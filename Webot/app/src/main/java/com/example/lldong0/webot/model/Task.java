package com.example.lldong0.webot.model;

public class Task {

    private String assignment;
    private String cognitiveError;
    private String automaticThought;
    private Boolean isChecked;
    private String writtenTime;

    public Task(){}

    public Task(String assignment, String cognitiveError, String automaticThought, Boolean isChecked, String writtenTime) {
        this.assignment = assignment;
        this.cognitiveError = cognitiveError;
        this.automaticThought = automaticThought;
        this.isChecked = isChecked;
        this.writtenTime = writtenTime;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getCognitiveError() {
        return cognitiveError;
    }

    public void setCognitiveError(String cognitiveError) {
        this.cognitiveError = cognitiveError;
    }

    public String getAutomaticThought() {
        return automaticThought;
    }

    public void setAutomaticThought(String automaticThought) {
        this.automaticThought = automaticThought;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getWrittenTime() {
        return writtenTime;
    }

    public void setWrittenTime(String writtenTime) {
        this.writtenTime = writtenTime;
    }
}
