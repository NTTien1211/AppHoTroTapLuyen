package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.BookModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.FeedbackModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PT_Over_PT_Adapter extends RecyclerView.Adapter<PT_Over_PT_Adapter.ViewHolder> {
    private List<BookModel> feedbackModel;
    private Context context;

    public PT_Over_PT_Adapter(List<BookModel> feedbackModel , Context context) {
        this.feedbackModel = feedbackModel;
        this.context = context;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.over_pt_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookModel feedback = feedbackModel.get(position);
        if (feedback.getUsers().getImg() == null){

        }else {
            Picasso.get().load(feedback.getUsers().getImg()).transform(new CircleTransform()).into(holder.img_user_inday);
        }
        holder.time_inday.setText(feedback.getTimein_day());
        holder.text_user_inday.setText(feedback.getUsers().getName());
    }


    @Override
    public int getItemCount() {
        return feedbackModel.size();
    }
    private String getItemIdAtPosition(int position) {
        return "Item ID " + position;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_user_inday;
        public TextView time_inday;
        public TextView text_user_inday;

        public ViewHolder(View itemView) {
            super(itemView);
            img_user_inday = itemView.findViewById(R.id.img_user_inday);
            time_inday = itemView.findViewById(R.id.time_inday);
            text_user_inday = itemView.findViewById(R.id.text_user_inday);

        }
    }
}
