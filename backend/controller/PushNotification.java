package com.example.doancuoiky.hostel.controller;

import com.example.doancuoiky.hostel.service.NotificationMessaging;
import com.example.doancuoiky.hostel.service.Response;
import com.example.doancuoiky.hostel.service.notificationPushImP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/pushNotification")
public class PushNotification {
    @Autowired
    private notificationPushImP notificationPushImP;

    @PostMapping("/push")
    public ResponseEntity<?> Push(@RequestBody NotificationMessaging notificationMessaging){
        Response response = notificationPushImP.sendNotificationByToken(notificationMessaging);
        if (response.isSuccess()){
            return new ResponseEntity<>(new Response(true," successfully"), HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + response.getMessage());
        }

    }
}
