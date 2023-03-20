package com.CPIS498.delanilltaqnia.models;

import java.util.List;
import java.util.Map;

public class ComputingField extends  BasicClass {
    String id;
    String title;
    String brief;
    String url;


    public ComputingField(String id, String title, String brief) {
        this.id = id;
        this.title = title;
        this.brief = brief;
    }

    public ComputingField() {
    }

    public ComputingField(Map<String,Object> map) {
        this.id=map.get("id").toString();
        this.title=map.get("title").toString();
        this.brief=map.get("brief").toString();
        this.url=map.get("url").toString();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
