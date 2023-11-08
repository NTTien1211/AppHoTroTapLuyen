package com.example.app_hotrotapluyen.gym.splash;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.app_hotrotapluyen.R;

import me.relex.circleindicator.CircleIndicator;

public class SplashActivity extends AppCompatActivity {
    private TextView textView;
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;
    private CircleIndicator circleIndicator;
    private  ViewPagerAdapter_splash viewPagerAdapter_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();

        viewPagerAdapter_splash = new ViewPagerAdapter_splash(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter_splash);

        circleIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==3){
                    textView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                }
                else textView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void init(){
        textView = findViewById(R.id.splash_skip);
        viewPager = findViewById(R.id.splash_viewpager);
        relativeLayout = findViewById(R.id.splash_layout_bottom) ;
        circleIndicator = findViewById(R.id.splash_circleIndicator);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem()<3){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
            }
        });
    }
}