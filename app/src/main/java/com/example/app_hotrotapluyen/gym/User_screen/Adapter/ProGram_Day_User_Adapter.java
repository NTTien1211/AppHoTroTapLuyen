package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.DayPRo_model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.HomeU_pt;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_Mess_Chat_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_PT_Inf_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_listPro_Day_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_listPro_ProgramChild_Activity;
import com.example.app_hotrotapluyen.gym.until.AndroidUtil;

import java.util.List;

public class ProGram_Day_User_Adapter extends RecyclerView.Adapter<ProGram_Day_User_Adapter.ViewHolder> {
    private List<DayPRo_model> dayPRo_model;
    private Context context;

    public ProGram_Day_User_Adapter(List<DayPRo_model> dayPRo_model) {
        this.dayPRo_model = dayPRo_model;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycl_listpro_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DayPRo_model item = dayPRo_model.get(position);
        holder.listpro_day_name.setText(item.getNameDay());
        holder.listpro_day_number.setText(String.valueOf(item.getSlbtap()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long itemID = item.getID_Day();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("GymTien",view.getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("id_idDay",itemID);
                editor.apply();
                Intent intent = new Intent(view.getContext(), User_listPro_ProgramChild_Activity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayPRo_model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView listpro_day_name;
        public TextView listpro_day_number;

        public ViewHolder(View itemView) {
            super(itemView);
            listpro_day_name = itemView.findViewById(R.id.listpro_day_name);
            listpro_day_number = itemView.findViewById(R.id.listpro_day_number);

        }

    }
}

