package com.example.app_hotrotapluyen.gym.splash;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter_splash extends FragmentStatePagerAdapter {

    public ViewPagerAdapter_splash(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SplashFragment_work();
            case 1:
                return new SplashFragment_food();
            case 2:
                return new SplashFragment_support();
            case 3:
                return new SplashFragment_quality();
            default:
                return new SplashFragment_work();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
