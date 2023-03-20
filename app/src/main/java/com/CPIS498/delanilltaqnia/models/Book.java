package com.CPIS498.delanilltaqnia.models;

import java.util.Map;

public class Book {
    String id;
    String title;
    String link;
    String cover_image;
    String computing_field;


    public Book(String id, String title, String link, String cover_image, String computing_field ) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.cover_image = cover_image;
        this.computing_field = computing_field;

    }

    public Book() {
    }

    public Book(Map<String,Object> map) {
        this.id=map.get("id").toString();
        this.title=map.get("title").toString();
        this.link=map.get("link").toString();
        this.cover_image=map.get("cover_image").toString();
        this.computing_field=map.get("computing_field").toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getComputing_field() {
        return computing_field;
    }

    public void setComputing_field(String filed_id) {
        this.computing_field = computing_field;
    }




}
