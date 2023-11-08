package com.example.app_hotrotapluyen.gym.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.app_hotrotapluyen.R;



public class SplashFragment_work extends Fragment {

    public SplashFragment_work(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_work, container, false);
    }
}