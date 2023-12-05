package com.example.doancuoiky.hostel.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class notificationPushImP implements notification {
    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Override
    public Response sendNotificationByToken(NotificationMessaging notificationMessaging){
        Notification notification = Notification.builder()
                .setTitle(notificationMessaging.getTitle())
                .setBody(notificationMessaging.getBody())
                .setImage(notificationMessaging.getImg())
                .build();
        Message message = Message.builder()
                .setToken(notificationMessaging.getToken())
                .setNotification(notification)
                .putAllData(notificationMessaging.getData())
                .build();
        try{
            firebaseMessaging.send(message);
            return new Response (true,"Success Sending Notification");
        }catch (FirebaseMessagingException exception){
            exception.printStackTrace();
            return new Response(false, "Error Sending Notification");
        }
    }
}
