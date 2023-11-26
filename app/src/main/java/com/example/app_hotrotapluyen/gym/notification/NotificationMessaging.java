package com.example.app_hotrotapluyen.gym.notification;

import java.util.Map;

public class NotificationMessaging {
    String token;
    String title;
    String body;
    String img;
    Map<String, String> data;


    public NotificationMessaging(String token, String title, String body, String img, Map<String, String> data) {
        this.token = token;
        this.title = title;
        this.body = body;
        this.img = img;
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}