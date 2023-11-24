package com.example.app_hotrotapluyen.gym.Admin.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.Admin.Admin_Approve_Registration_Activity;
import com.example.app_hotrotapluyen.gym.Admin.Admin_Browser_End_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.BookModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Admin_Over_Br_Adapter extends RecyclerView.Adapter<Admin_Over_Br_Adapter.ViewHolder> {
    private List<BookModel> userList;
    private Context context;

    public Admin_Over_Br_Adapter(List<BookModel> userList , Context context) {
        this.userList = userList;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_browser_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            BookModel user = userList.get(position);
            // Hiển thị thông tin cho Level 1
            holder.name_pt_admin_brower.setText(user.getUser_Give().getName());
            Picasso.get().load(user.getUser_Give().getImg()).transform(new CircleTransform()).into(holder.profile_pic_image_view_repairt_profile_pt);

            // Hiển thị thông tin cho Level 0
            holder.name_user_admin_brower.setText(user.getUsers().getName());
            Picasso.get().load(user.getUsers().getImg()).transform(new CircleTransform()).into(holder.profile_pic_image_view_repairt_profile_us);
            Long idBook = user.getID_Book();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Admin_Browser_End_Activity.class);
                intent.putExtra("ID_Book_ts", idBook);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    private String getItemIdAtPosition(int position) {
        return "Item ID " + position;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_pt_admin_brower , name_user_admin_brower,
                day_browser_admin_brower;
        private ImageView profile_pic_image_view_repairt_profile_us ,
                profile_pic_image_view_repairt_profile_pt;
        LinearLayout layoutForLevel0 ,layoutForLevel1 ;


        public ViewHolder(View itemView) {
            super(itemView);
            name_pt_admin_brower = itemView.findViewById(R.id.name_pt_admin_brower);
            name_user_admin_brower = itemView.findViewById(R.id.name_user_admin_brower);


            profile_pic_image_view_repairt_profile_us = itemView.findViewById(R.id.profile_pic_image_view_repairt_profile_us);
            profile_pic_image_view_repairt_profile_pt = itemView.findViewById(R.id.profile_pic_image_view_repairt_profile_pt);
        }
    }
}
