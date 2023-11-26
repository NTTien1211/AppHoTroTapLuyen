package com.example.app_hotrotapluyen.gym.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("pushNotification/push")
    Call<Response> sendNotification(@Body NotificationMessaging notificationMessaging);
}
