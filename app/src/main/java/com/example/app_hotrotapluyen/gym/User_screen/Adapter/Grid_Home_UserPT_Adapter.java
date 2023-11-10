package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.HomeU_pt;

import java.util.List;

public class Grid_Home_UserPT_Adapter extends RecyclerView.Adapter<Grid_Home_UserPT_Adapter.ViewHolder> {
    private List<HomeU_pt> userList;

    public Grid_Home_UserPT_Adapter(List<HomeU_pt> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyc_pt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeU_pt user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewExperience.setText("Experience: " + user.getExperience());
        holder.textViewManagers.setText("Managers: " + user.getManagers());
        holder.textViewRate.setText("Rate: " + user.getRate());
    }

    @Override
    public int getItemCount() {
        return userList.size();
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

