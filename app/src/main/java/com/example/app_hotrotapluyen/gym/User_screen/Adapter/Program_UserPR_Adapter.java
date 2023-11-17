package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.HomeU_pt;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_Mess_Chat_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_PT_Inf_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_listPro_Day_Activity;
import com.example.app_hotrotapluyen.gym.until.AndroidUtil;

import java.util.List;

public class Program_UserPR_Adapter extends RecyclerView.Adapter<Program_UserPR_Adapter.ViewHolder> {
    private List<ProgramModel> programModels;
    private Context context;

    public Program_UserPR_Adapter(List<ProgramModel> programModels) {
        this.programModels = programModels;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profoo_program, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProgramModel item = programModels.get(position);
        holder.name_profoo_program.setText(item.getNamePro());
        int id = item.getID_Pro();
        holder.nameUS_profoo_program.setText(item.getNameUserAdd());
        holder.level_profoo_program.setText(item.getLevel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemID = item.getID_Pro();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("GymTien",view.getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("ID_Pro",id);
                editor.apply();
                Intent intent = new Intent(view.getContext(), User_listPro_Day_Activity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return programModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_profoo_program;
        public TextView nameUS_profoo_program;
        public TextView number_program;
        public TextView level_profoo_program;

        public ViewHolder(View itemView) {
            super(itemView);
            name_profoo_program = itemView.findViewById(R.id.name_profoo_program);
            nameUS_profoo_program = itemView.findViewById(R.id.nameUS_profoo_program);
//            number_program = itemView.findViewById(R.id.number_program);
            level_profoo_program = itemView.findViewById(R.id.level_profoo_program);

        }
    }
}

