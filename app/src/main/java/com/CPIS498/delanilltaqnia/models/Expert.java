package com.CPIS498.delanilltaqnia.models;

import java.util.HashMap;
import java.util.Map;

public class Expert {
    String name;
    String brief;
    String job_title;
    Map<String,String> social_links;
    String computing_field;

    public Expert() {
    }

    public Expert(Map<String,Object> map) {
        this.name=map.get("name").toString();
        this.brief=map.get("brief").toString();
        this.job_title=map.get("job_title").toString();
        this.social_links= (Map<String, String>) map.get("social_links");
        this.computing_field=map.get("computing_field").toString();

    }

    public Expert(String id, String name, String brief, String job_title, String photo, Map<String, String> social_links, String computing_field) {
        this.name = name;
        this.brief = brief;
        this.job_title = job_title;
        this.social_links = social_links;
        this.computing_field = computing_field;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }




    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }


    public Map<String, String> getSocial_links() {
        return social_links;
    }

    public void setSocial_links(Map<String, String> social_links) {
        this.social_links = social_links;
    }

    public String getComputing_field() {
        return computing_field;
    }

    public void setComputing_field(String computing_field) {
        this.computing_field = computing_field;
    }

}
