package com.example.app_hotrotapluyen.gym.login_regis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_hotrotapluyen.R;

import java.util.Calendar;

public class Information_regist_old_Activity extends AppCompatActivity {
    NumberPicker numberPicker;
    Button nextButton_infor_old;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_regist_old);

        until();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        numberPicker.setMinValue(1950);
        numberPicker.setMaxValue(currentYear);
        numberPicker.setWrapSelectorWheel(true);

        nextButton_infor_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Information_regist_old_Activity.this , Information_regist_weight_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void until() {
        numberPicker = findViewById(R.id.numberPicker_old);
        nextButton_infor_old = findViewById(R.id.nextButton_infor_old);
    }

}