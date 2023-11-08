package com.example.app_hotrotapluyen.gym.User_screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Search_UserRecy_MessAdapter  extends FirestoreRecyclerAdapter<User, Search_UserRecy_MessAdapter.UserViewHolder> {
    Context context;
    public Search_UserRecy_MessAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        holder.userName.setText(model.getName());
        holder.userPhone.setText(model.getPhone());
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mess_user,parent,false);
        return new UserViewHolder(view);
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView userName, userPhone;
        ImageView profileView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.Mess_userName_text);
            userPhone = itemView.findViewById(R.id.Mess_userPhone_text);
            profileView = itemView.findViewById(R.id.profile_pic_image_view);

        }
    }
}
