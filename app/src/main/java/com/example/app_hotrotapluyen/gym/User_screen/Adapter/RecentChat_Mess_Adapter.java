package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ChatMessModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ChatRoomModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_Mess_Chat_Activity;
import com.example.app_hotrotapluyen.gym.until.AndroidUtil;
import com.example.app_hotrotapluyen.gym.until.FirebaseUntil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecentChat_Mess_Adapter  extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChat_Mess_Adapter.ChatRoomModeViewHolder> {
    Context context;
    String userid;

    public RecentChat_Mess_Adapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context, String userid) {
            super(options);
        this.context = context;
        this.userid = userid;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModeViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FirebaseUntil.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(userid);
                        UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                        // Perform null check before setting the user name
                        if (otherUserModel != null && otherUserModel.getName() != null) {
                            holder.userName.setText(otherUserModel.getName());
                        } else {
                            holder.userName.setText("Unknown User");
                        }

                        if (lastMessageSentByMe)
                            holder.lastmessText.setText("You : " + model.getLastMessage());
                        else
                            holder.lastmessText.setText(model.getLastMessage());
                        holder.lassmessTime.setText(FirebaseUntil.timestampToString(model.getLastMessageTimestamp()));

                        holder.itemView.setOnClickListener(v -> {
                            //navigate to chat activity
                            Intent intent = new Intent(context, User_Mess_Chat_Activity.class);
                            AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });

                    }
                });
    }


    @NonNull
    @Override
    public ChatRoomModeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycl_chat_user_row,parent,false);
        return new ChatRoomModeViewHolder(view);
    }

    class ChatRoomModeViewHolder extends RecyclerView.ViewHolder{
        TextView userName , lastmessText, lassmessTime;
        ImageView profileView;
        public ChatRoomModeViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_text);
            lastmessText = itemView.findViewById(R.id.last_message_text);
            lassmessTime = itemView.findViewById(R.id.last_message_time_text);
            profileView = itemView.findViewById(R.id.profile_pic_image_view);

        }
    }
}
