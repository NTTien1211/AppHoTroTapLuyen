package com.example.app_hotrotapluyen.gym.until;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUntil {

    public static String currentUserId(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }


    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1, String userId2) {
        if (userId1 != null && userId2 != null) {
            if (userId1.hashCode() < userId2.hashCode()) {
                return userId1 + "_" + userId2;
            } else {
                return userId2 + "_" + userId1;
            }
        } else {
            // Xử lý trường hợp mà hoặc userId1 hoặc userId2 là null
            return "defaultChatroomId"; // Bạn có thể thay thế điều này bằng giá trị mặc định hoặc xử lý theo nhu cầu của ứng dụng
        }
    }


    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static void getOtherUserFromChatroom(List<String> userIds, OnUserFetchListener listener) {
        String otherUserId = (userIds.get(0).equals(currentUserId())) ? userIds.get(1) : userIds.get(0);
        DocumentReference otherUserRef = allUserCollectionReference().document(otherUserId);

        otherUserRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserModel otherUserModel = task.getResult().toObject(UserModel.class);
                if (listener != null) {
                    listener.onUserFetched(otherUserModel);
                }
            } else {
                // Xử lý lỗi
            }
        });
    }

    public static DocumentReference getUserFromId(String userId) {
        // Assuming 'users' is the collection name where user information is stored
        // Adjust the collection name based on your Firestore setup
        return FirebaseFirestore.getInstance().collection("users").document(userId);
    }
    // Định nghĩa một giao diện để xử lý callback khi thông tin người dùng được lấy về
    public interface OnUserFetchListener {
        void onUserFetched(UserModel user);
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference  getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUntil.currentUserId());
    }

    public static StorageReference  getOtherProfilePicStorageRef(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(otherUserId);
    }

}