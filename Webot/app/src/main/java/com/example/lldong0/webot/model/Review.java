package com.example.lldong0.webot.model;

public class Review {
    private String userUid;
    private String userEmail;
    private String contents;
    private String writtenTime;

    public Review() {
    }

    public Review(String userUid, String userEmail, String contents, String writtenTime) {
        this.userUid = userUid;
        this.userEmail = userEmail;
        this.contents = contents;
        this.writtenTime = writtenTime;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWrittenTime() {
        return writtenTime;
    }

    public void setWrittenTime(String writtenTime) {
        this.writtenTime = writtenTime;
    }
}
