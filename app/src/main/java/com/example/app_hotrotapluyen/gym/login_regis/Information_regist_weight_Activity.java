package com.example.app_hotrotapluyen.gym.login_regis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_hotrotapluyen.R;

import java.util.Calendar;

public class Information_regist_weight_Activity extends AppCompatActivity {
    NumberPicker numberPicker;
    Button nextButton_infor_weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_regist_weight);
        until();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        numberPicker.setMinValue(1950);
        numberPicker.setMaxValue(currentYear);
        numberPicker.setWrapSelectorWheel(true);

        nextButton_infor_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Information_regist_weight_Activity.this , Informatin_regist_height_Activity.class);
//                startActivity(intent);
            }
        });
    }

    private void until() {
        numberPicker = findViewById(R.id.numberPicker_weight);
        nextButton_infor_weight = findViewById(R.id.nextButton_infor_weight);
    }
}