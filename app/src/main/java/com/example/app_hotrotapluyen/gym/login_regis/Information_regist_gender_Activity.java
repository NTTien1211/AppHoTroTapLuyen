package com.example.app_hotrotapluyen.gym.login_regis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_hotrotapluyen.R;

public class Information_regist_gender_Activity extends AppCompatActivity {
    Button nextButton_infor_gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_regist_gender);

        until();
        nextButton_infor_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Information_regist_gender_Activity.this, Information_regist_old_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void until() {
        nextButton_infor_gender = findViewById(R.id.nextButton_infor_gender);
    }
}