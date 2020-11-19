package com.example.youtubesubscriptionmanager;

public class Channel {
    private String name;
    private String id;
    private String subtext;
    private String imgURL;

    Channel(String name, String id, String subtext, String imgURL) {
        this.name = name;
        this.id = id;
        this.subtext = subtext;
        this.imgURL = imgURL;
    }

    public void setName(String name) { this.name = name; };
    public void setId(String id) { this.id = id; };
    public void setSubtext(String subtext) { this.subtext = subtext; };
    public void setImgURL(String imgURL) { this.imgURL = imgURL; };

    public String getName() { return this.name; };
    public String getId() { return this.id; };
    public String getSubtext() { return this.subtext; };
    public String getImgURL() { return this.imgURL; };
}
