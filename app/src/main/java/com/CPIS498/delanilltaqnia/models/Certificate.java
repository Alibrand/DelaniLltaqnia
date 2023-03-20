package com.CPIS498.delanilltaqnia.models;

import java.util.Map;

public class Certificate {
    String id;
    String title;
    String brief;
    String logo;
    String url;
    String computing_field;


    public Certificate() {
    }

    public Certificate(String id, String title, String brief, String logo, String url, String computing_field  ) {
        this.id = id;
        this.title = title;
        this.brief = brief;
        this.logo = logo;
        this.url = url;
        this.computing_field = computing_field;

    }

    public Certificate(Map<String,Object> map) {
        this.id=map.get("id").toString();
        this.title=map.get("title").toString();
        this.brief=map.get("brief").toString();
        this.url=map.get("url").toString();
        this.logo=map.get("logo").toString();
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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComputing_field() {
        return computing_field;
    }

    public void setComputing_field(String field_id) {
        this.computing_field = field_id;
    }

}
