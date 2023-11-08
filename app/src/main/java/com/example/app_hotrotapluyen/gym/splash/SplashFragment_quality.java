package com.example.app_hotrotapluyen.gym.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.login_regis.LoginActivity;


public class SplashFragment_quality extends Fragment {

    private View mView;
    private Button btn_splash;
    public SplashFragment_quality() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_splash_quality, container, false);

        btn_splash =mView.findViewById(R.id.button_splash);
        btn_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
            }
        });
        return mView;
    }
}