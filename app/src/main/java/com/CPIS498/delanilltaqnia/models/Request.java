package com.CPIS498.delanilltaqnia.models;

import com.google.type.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Request extends BasicClass {
    String id;
    Date request_date;
    String request_user;
    String request_type;
    Map<String,String> request_data;

    public Request(){

    }

    public Request(String id, Date request_date, String request_user, String request_type, Map<String, String> request_data) {
        this.id = id;
        this.request_date = request_date;
        this.request_user = request_user;
        this.request_type = request_type;
        this.request_data = request_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getRequest_date() {
        return request_date;
    }

    public void setRequest_date(Date request_date) {
        this.request_date = request_date;
    }

    public String getRequest_user() {
        return request_user;
    }

    public void setRequest_user(String request_user) {
        this.request_user = request_user;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public Map<String, String> getRequest_data() {
        return request_data;
    }

    public void setRequest_data(Map<String, String> request_data) {
        this.request_data = request_data;
    }


}
