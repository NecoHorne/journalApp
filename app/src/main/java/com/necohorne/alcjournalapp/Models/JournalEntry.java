package com.necohorne.alcjournalapp.Models;

public class JournalEntry {

    private String Title;
    private String Body;
    private String Date;
    private String UserId;
    private int dataBasePos;

    public JournalEntry(String title, String body, String date, String userId) {
        Title = title;
        Body = body;
        Date = date;
        UserId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getDataBasePos() {
        return dataBasePos;
    }

    public void setDataBasePos(int dataBasePos) {
        this.dataBasePos = dataBasePos;
    }
}
