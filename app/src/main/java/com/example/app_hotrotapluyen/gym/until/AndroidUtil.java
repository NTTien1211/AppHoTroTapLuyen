package com.example.app_hotrotapluyen.gym.until;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;

import com.google.firebase.firestore.auth.User;

public class AndroidUtil {

    public static  void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getName());
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("userId",model.getIdUser());
//        intent.putExtra("fcmToken",model.getFcmToken());

    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setName(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setIdUser(intent.getStringExtra("userId"));
//        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;
    }

//    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
//        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
//    }
}