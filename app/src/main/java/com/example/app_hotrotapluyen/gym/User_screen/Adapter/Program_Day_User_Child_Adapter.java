package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.DayPRo_model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.HomeU_pt;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.Program_child_Model;
import com.example.app_hotrotapluyen.gym.User_screen.User_Mess_Chat_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_PT_Inf_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_listPro_Day_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_listPro_ProgramChild_Activity;
import com.example.app_hotrotapluyen.gym.until.AndroidUtil;

import java.util.List;

public class Program_Day_User_Child_Adapter extends RecyclerView.Adapter<Program_Day_User_Child_Adapter.ViewHolder> {
    private List<Program_child_Model> program_child_Model;
    private Context context;

    public Program_Day_User_Child_Adapter(List<Program_child_Model> program_child_Model) {
        this.program_child_Model = program_child_Model;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycl_lispro_childay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program_child_Model item = program_child_Model.get(position);
        holder.listpro_dayChild_name.setText(item.getNameProChi());
        // Assuming item.getImg() now returns a URL or resource ID
//        Picasso.get().load(item.getImg()).into(holder.img_proChild);

        holder.listpro_dayChiil_unil.setText(item.getUnil());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long itemID = item.getID_Program_Child();
////                Intent intent = new Intent(context, User_listPro_ProgramChild_Activity.class);
////                intent.putExtra("id_item" , itemID);
////                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return program_child_Model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_proChild;
        TextView listpro_dayChild_name, listpro_dayChiil_unil;
        
        public ViewHolder(View itemView) {
            super(itemView);

            img_proChild = itemView.findViewById(R.id.img_proChild);
            listpro_dayChild_name = itemView.findViewById(R.id.listpro_dayChild_name);
            listpro_dayChiil_unil = itemView.findViewById(R.id.listpro_dayChiil_unil);


        }

    }
}

