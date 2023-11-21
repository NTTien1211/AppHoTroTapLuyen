package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.app_hotrotapluyen.R;

public class User_Repair_Inf_Activity extends AppCompatActivity {
    private SeekBar decimalSeekBar;
    private TextView valueTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_repair_inf);

        NumberPicker verticalNumberPicker = findViewById(R.id.verticalNumberPicker);

        Toolbar actionBar = findViewById(R.id.toolbar_userrepair);
        setToolbar(actionBar, "");
        verticalNumberPicker.setMinValue(130);
        verticalNumberPicker.setMaxValue(250);
        verticalNumberPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị khi cuộn

        // Đặt bước (step) cho NumberPicker dọc là 1
        String[] displayedValues = new String[121];
        for (int i = 0; i <= 120; i++) {
            displayedValues[i] = String.valueOf(130 + i);
        }
        verticalNumberPicker.setDisplayedValues(displayedValues);

        decimalSeekBar = findViewById(R.id.decimalSeekBar);
        valueTextView = findViewById(R.id.valueTextView);

        decimalSeekBar.setMax(5000); // Số bước (steps) có thể tùy chỉnh tùy theo yêu cầu của bạn

        decimalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Chia nhỏ giá trị SeekBar thành số thập phân
                double decimalValue = progress / 10.0;

                // Hiển thị giá trị số thập phân trong TextView hoặc thực hiện xử lý tương ứng
                valueTextView.setText(String.valueOf(decimalValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Xử lý khi bắt đầu chạm vào SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Xử lý khi kết thúc chạm vào SeekBar
            }
        });
    }

    private void setToolbar(Toolbar toolbar, String name){
        setSupportActionBar(toolbar);
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spannableString);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}