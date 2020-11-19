package com.example.youtubesubscriptionmanager;

public class Video {
    private String title;
    private String id;
    private String date;
    private String URL;
    private String imgURL;
    private String upcoming;

    Video(String title, String id, String date, String imgURL, String upcoming) {
        this.title = title;
        this.id = id;
        this.date = date;
        this.imgURL = imgURL;
        this.upcoming = upcoming;
    }

    public String getTitle() { return title; }
    public String getId() { return id; }
    public String getDate() { return date; }
    public String getImgURL() { return imgURL; }
    public String getUpcoming() { return upcoming; }

    public void setTitle(String title) { this.title = title; }
    public void setId(String id) { this.id = id; }
    public void setDate(String date) { this.date = date; }
    public void setImgURL(String imgURL) { this.imgURL = imgURL; }
    public void setUpcoming(String upcoming) { this.upcoming = upcoming; }
}
