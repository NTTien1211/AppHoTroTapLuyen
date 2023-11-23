package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.app_hotrotapluyen.R;

public class User_Follow_Us_Activity extends AppCompatActivity {
    TextView intagram , facebook , linkdin  , playstore ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_follow_us);
        Toolbar actionBar = findViewById(R.id.tobar_floowus);
        setToolbar(actionBar, "");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhxa();
        intagram.setMovementMethod(LinkMovementMethod.getInstance());
        facebook.setMovementMethod(LinkMovementMethod.getInstance());
        linkdin.setMovementMethod(LinkMovementMethod.getInstance());
        playstore.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private void anhxa() {
        intagram = findViewById(R.id.instagram_link);
        facebook = findViewById(R.id.facebook_link);
        linkdin = findViewById(R.id.linkdin_link);
        playstore= findViewById(R.id.playstore_link);
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