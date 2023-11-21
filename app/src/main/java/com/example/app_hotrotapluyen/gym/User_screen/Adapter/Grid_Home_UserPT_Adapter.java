package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_Mess_Chat_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_PT_Inf_Activity;
import com.example.app_hotrotapluyen.gym.until.AndroidUtil;

import java.util.List;

public class Grid_Home_UserPT_Adapter extends RecyclerView.Adapter<Grid_Home_UserPT_Adapter.ViewHolder> {
    private List<UserModel> userList;
    private Context context;

    public Grid_Home_UserPT_Adapter(List<UserModel> userList , Context context) {
        this.userList = userList;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyc_pt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewExperience.setText("Experience: " + user.getExperience());
        holder.textViewManagers.setText("Managers: " + user.getManagers());
        holder.textViewRate.setText("Rate: " + user.getRate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    UserModel user = userList.get(adapterPosition);
                    // Truyền ID của item sang User_Mess_Chat_Activity
                    Intent intent = new Intent(context, User_PT_Inf_Activity.class);
                    intent.putExtra("PT_ID", user.getIdUser());
                    context.startActivity(intent);
                }
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
        public TextView textViewExperience;
        public TextView textViewManagers;
        public TextView textViewRate;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName_HomeU);
            textViewExperience = itemView.findViewById(R.id.exper_pt_homeU);
            textViewManagers = itemView.findViewById(R.id.member_homeU);
            textViewRate = itemView.findViewById(R.id.rate_homeU);
        }
    }
}

