package com.example.app_hotrotapluyen.gym.PTrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.app_hotrotapluyen.R;

public class PTrainer_Update_Level_Activity extends AppCompatActivity {
    LinearLayout parentLayout;
    ImageButton  add_prize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptrainer_update_level);
        anhxa();
        add_prize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm một LinearLayout mới
                addNewLinearLayout();
            }
        });
    }

    private void addNewLinearLayout() {
        LinearLayout newLayout = new LinearLayout(PTrainer_Update_Level_Activity.this);
        newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText newEditText = new EditText(PTrainer_Update_Level_Activity.this);
        LinearLayout.LayoutParams paramsEditText = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        newEditText.setLayoutParams(paramsEditText);
        // Đặt các thuộc tính khác cho EditText theo nhu cầu

        ImageView newImageView = new ImageView(PTrainer_Update_Level_Activity.this);
        LinearLayout.LayoutParams paramsImageView = new LinearLayout.LayoutParams(
                0,
                150 // Đặt chiều cao theo nhu cầu
        );
        newImageView.setLayoutParams(paramsImageView);
        // Đặt các thuộc tính khác cho ImageView theo nhu cầu

        newLayout.addView(newEditText);
        newLayout.addView(newImageView);

        parentLayout.addView(newLayout);

    }


    private void anhxa() {
        parentLayout = findViewById(R.id.layout_prize);
        add_prize = findViewById(R.id.add_prize);
    }
}