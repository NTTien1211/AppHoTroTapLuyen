package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.app_hotrotapluyen.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class User_Main_Activity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        final Fragment HomeU = new User_Home_Fragment();
        final Fragment MessU = new User_Mess_Fragment();
        loadFragment(HomeU);
        bottomNavigationView =findViewById(R.id.menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.menu_Home){
                    selectedFragment =  HomeU;
                }else if (item.getItemId() == R.id.menu_Mess){
                    selectedFragment = MessU ;
                }
                loadFragment(selectedFragment);
                return true;
            }
        });


    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}