package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.FeedbackModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_PT_Inf_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Rate_Feedback_PT_Adapter extends RecyclerView.Adapter<Rate_Feedback_PT_Adapter.ViewHolder> {
    private List<FeedbackModel> feedbackModel;
    private Context context;

    public Rate_Feedback_PT_Adapter(List<FeedbackModel> feedbackModel , Context context) {
        this.feedbackModel = feedbackModel;
        this.context = context;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_cmt_rate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedbackModel feedback = feedbackModel.get(position);
        if (feedback.getUsers().getImg() == null){

        }else {
            Picasso.get().load(feedback.getUsers().getImg()).transform(new CircleTransform()).into(holder.icon_img_user_feedback);
        }
        holder.text_user_feedback.setText(feedback.getUsers().getName());
        holder.infor_user_feedback.setText(feedback.getInformation());
    }


    @Override
    public int getItemCount() {
        return feedbackModel.size();
    }
    private String getItemIdAtPosition(int position) {
        return "Item ID " + position;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon_img_user_feedback;
        public TextView text_user_feedback;
        public TextView infor_user_feedback;
        public TextView time_user_feedback;

        public ViewHolder(View itemView) {
            super(itemView);
            icon_img_user_feedback = itemView.findViewById(R.id.icon_img_user_feedback);
            text_user_feedback = itemView.findViewById(R.id.text_user_feedback);
            infor_user_feedback = itemView.findViewById(R.id.infor_user_feedback);
            time_user_feedback = itemView.findViewById(R.id.time_user_feedback);
        }
    }
}
