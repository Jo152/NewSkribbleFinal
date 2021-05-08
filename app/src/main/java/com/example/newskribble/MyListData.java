package com.example.newskribble;

public class MyListData{

    private String title;
    private String content;

    public MyListData(){
        // empty constructor required for firebase.
    }

    public MyListData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}