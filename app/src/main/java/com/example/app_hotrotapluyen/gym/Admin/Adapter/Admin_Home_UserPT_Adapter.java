package com.example.app_hotrotapluyen.gym.Admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.Admin.Admin_Approve_Registration_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Admin_Home_UserPT_Adapter extends RecyclerView.Adapter<Admin_Home_UserPT_Adapter.ViewHolder> {
    private List<UserModel> userList;
    private Context context;

    public Admin_Home_UserPT_Adapter(List<UserModel> userList , Context context) {
        this.userList = userList;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application_pt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModel user = userList.get(position);
        String id = user.getIdUser();
        holder.textViewName.setText(user.getName());
        holder.textViewExperience.setText("Experience: " + user.getExperience());
        holder.textViewBMi.setText("BMI: " + user.getBMI());
        String url = user.getImg();
        Log.d("TAG", "onBindViewHolder: " +url);
        Picasso.get().load(url).transform(new CircleTransform()).into(holder.recycle_img_applica);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context, Admin_Approve_Registration_Activity.class);
                    intent.putExtra("PT_ID", id);
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
        public TextView textViewName;
        private ImageView recycle_img_applica;
        public TextView textViewExperience;
        public TextView textViewBMi;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName_HomeU);
            textViewExperience = itemView.findViewById(R.id.exper_pt_homeU);
            textViewBMi = itemView.findViewById(R.id.member_homeU_bmi);
            recycle_img_applica = itemView.findViewById(R.id.recycle_img_applica);
        }
    }
}

