package com.example.doancuoiky.hostel.service;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationMessaging {
    String token;
    String title;
    String body;
    String img;
    Map<String,String> data;
}
