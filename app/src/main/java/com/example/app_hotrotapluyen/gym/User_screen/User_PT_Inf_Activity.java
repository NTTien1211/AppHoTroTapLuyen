package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;

public class User_PT_Inf_Activity extends AppCompatActivity {
    ImageButton back;
    String idPT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pt_inf);
        back = findViewById(R.id.Pt_User_infor_back);
        idPT = getIntent().getExtras().getString("PT_ID");
        Toast.makeText(this, "" +idPT, Toast.LENGTH_SHORT).show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}