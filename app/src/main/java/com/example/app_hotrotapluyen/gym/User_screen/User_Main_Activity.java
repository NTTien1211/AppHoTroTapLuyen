package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.login_regis.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class User_Main_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    String idUser;
    TextView nameLeft, emailLeft , phoneLeft;
    DrawerLayout drawerLayout ;
    Toolbar toolbar ;
    UserModel userModel;
    NavigationView navigationView;
    TextView logout_av;
    private static final int Fagment_food = 0;
    int mCurrenFagment;
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
        final Fragment List = new User_listProFoo_Fragment();
        final Fragment User = new User_Profile_Fragment();
        loadFragment(HomeU);
        bottomNavigationView =findViewById(R.id.menu);
        toolbar = findViewById(R.id.toobal_main);
        drawerLayout =findViewById(R.id.Drawlayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(User_Main_Activity.this , drawerLayout, toolbar ,
                                            R.string.navigation_draw_open , R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigrionbar_main);
        navigationView.setNavigationItemSelectedListener(User_Main_Activity.this);
        View headerView = navigationView.getHeaderView(0);
        nameLeft = headerView.findViewById(R.id.UserName_UserMain);
        emailLeft = headerView.findViewById(R.id.Gmai_UserMain);
        phoneLeft = headerView.findViewById(R.id.Phone_UserMain);
        logout_av = findViewById(R.id.logout_av);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.menu_Home){
                    selectedFragment =  HomeU;
                }else if (item.getItemId() == R.id.menu_Mess){
                    selectedFragment = MessU ;
                }else if (item.getItemId() == R.id.menu_prog){
                    selectedFragment = List ;
                }else if (item.getItemId() == R.id.menu_user) {
                    selectedFragment = User;
                }
                loadFragment(selectedFragment);
                return true;
            }
        });

        logout_av.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Main_Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        Fragment selectedFragment = null;
        if(item.getItemId() == R.id.menu_food){
            selectedFragment = new User_listProFoo_Food_Fragment();

        }

        replaceFagment(selectedFragment);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    private  void  replaceFagment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout , fragment);
        transaction.commit();
    }
    private class  SelecDatabase extends AsyncTask<String, Void, UserModel> {
        @Override
        protected UserModel  doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT * FROM Users WHERE Id_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1,idUser );

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String id = resultSet.getString("ID_User");
                        String Name = resultSet.getString("Name");
                        String Email = resultSet.getString("Email");
                        String Phone = resultSet.getString("Phone");
                        String img = resultSet.getString("IMG");
                        userModel = new UserModel(id, Name, Email , Phone, img);
                        return userModel;
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
            return userModel;
        }
        @Override
        protected void onPostExecute(UserModel result) {
            super.onPostExecute(result);
            if (result!= null) {
                    nameLeft.setText(result.getName());
                    emailLeft.setText(result.getEmail());
                    phoneLeft.setText(result.getPhone());
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