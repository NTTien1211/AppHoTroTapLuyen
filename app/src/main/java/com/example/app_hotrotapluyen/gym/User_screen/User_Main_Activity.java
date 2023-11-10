package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User_Main_Activity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute(idUser);
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

    private class  SelecDatabase extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Name, Phone FROM Users WHERE Id_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1,idUser );

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                       return true;
                    } else {
                        return false;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(User_Main_Activity.this, "User found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(User_Main_Activity.this, "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}