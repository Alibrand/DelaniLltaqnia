package com.CPIS498.delanilltaqnia.models;

import java.util.Map;

public class Course {
    String id;
    String title;
    String logo;
    String computing_field;
    String link;


    public Course() {
    }

    public Course(Map<String, Object> map) {
        this.id = map.get("id").toString();
        this.title = map.get("title").toString();
        this.logo = map.get("logo").toString();
        this.computing_field = map.get("computing_field").toString();
        this.link = map.get("link").toString();

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getComputing_field() {
        return computing_field;
    }

    public void setComputing_field(String computing_field) {
        this.computing_field = computing_field;
    }
}
