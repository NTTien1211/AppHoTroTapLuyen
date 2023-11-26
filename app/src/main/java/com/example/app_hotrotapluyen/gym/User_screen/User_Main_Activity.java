package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.Admin.Admin_Home_Fragment;
import com.example.app_hotrotapluyen.gym.Admin.Admin_Over_Browser_Fragment;
import com.example.app_hotrotapluyen.gym.PTrainer.PTrainer_Update_Level_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.login_regis.LoginActivity;
import com.example.app_hotrotapluyen.gym.notification.AlertDialogManager;
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
    TextView nameLeft, emailLeft , phoneLeft ,levelAcc;
    DrawerLayout drawerLayout ;
    Toolbar toolbar ;
    UserModel userModel;
    NavigationView navigationView;
    TextView logout_av;
    String bmii;
    private static final int Fagment_food = 0;
    int mCurrenFagment;
    final Fragment HomeU = new User_Home_Fragment();
    final Fragment HomeAdmin = new Admin_Home_Fragment();
    final Fragment MessU = new User_Mess_Fragment();
    final Fragment List = new User_listProFoo_Fragment();
    final Fragment User = new User_Profile_Fragment();
    final Fragment OverAdmin = new Admin_Over_Browser_Fragment();
    final Fragment OverU = new User_Over_Fragment();
    String level;
    AlertDialogManager alertDialogManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        level = sharedPreferences.getString("levelID" ,"");
        SelecDatabase selecDatabase = new SelecDatabase();

        selecDatabase.execute(idUser);
        alertDialogManager = new AlertDialogManager();

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
        levelAcc = headerView.findViewById(R.id.levelAcc);
        logout_av = findViewById(R.id.logout_av);
        int colorAccent = ContextCompat.getColor(this, R.color.xanhla);
        int colorAccent1 = ContextCompat.getColor(this, R.color.bmi_normal_weight);
        int colorAccent2 = ContextCompat.getColor(this, R.color.colorAccent);

        if (Integer.parseInt(level) == 2 ){
            levelAcc.setText("AD");
            levelAcc.setTextColor(colorAccent);
            loadFragment(HomeAdmin);
            navigationView.getMenu().findItem(R.id.menu_Update_pt).setVisible(false);

        }else if (Integer.parseInt(level) == 1 ) {
            levelAcc.setText("PT");
            levelAcc.setTextColor(colorAccent1);
            loadFragment(HomeU);
            navigationView.getMenu().findItem(R.id.menu_Update_pt).setVisible(false);
        }
        else {
            levelAcc.setText("US");
            levelAcc.setTextColor(colorAccent2);
            loadFragment(HomeU);
            navigationView.getMenu().findItem(R.id.menu_Update_pt).setVisible(true);

        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.menu_Home){
                    if (Integer.parseInt(level) ==2 ){
                        selectedFragment =  HomeAdmin;
                    }
                    else {
                        selectedFragment =  HomeU;
                    }

                }else if (item.getItemId() == R.id.menu_Mess){
                    selectedFragment = MessU ;
                }else if (item.getItemId() == R.id.menu_prog){
                    selectedFragment = List ;
                }else if (item.getItemId() == R.id.menu_user) {
                    selectedFragment = User;
                }else  if(item.getItemId() == R.id.menu_Over){
                    if (Integer.parseInt(level) == 2 ){
                        selectedFragment =  OverAdmin;
                    }
                    else {
                        selectedFragment =  OverU;
                    }

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

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Xử lý sự kiện nhận được khi activity2 gửi broadcast
            String action = intent.getAction();
            if ("refresh_activity1_event".equals(action)) {
                // Thực hiện các hành động làm mới ở đây
                loadData();
            }
        }
    };

    private void loadData() {
        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute(idUser);
    }

    protected void onResume() {
        super.onResume();
        // Đăng ký BroadcastReceiver để lắng nghe sự kiện refresh từ activity2
        IntentFilter intentFilter = new IntentFilter("refresh_activity1_event");
        LocalBroadcastManager.getInstance(this).registerReceiver(refreshReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy đăng ký BroadcastReceiver khi activity1 tạm dừng
        LocalBroadcastManager.getInstance(this).unregisterReceiver(refreshReceiver);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        final Fragment[] selectedFragment = {null};
        if(item.getItemId() == R.id.menu_food){
            selectedFragment[0] = new User_listProFoo_Food_Fragment();

        }else if(item.getItemId() == R.id.menu_Update_pt){
                if (bmii==null || bmii.isEmpty()){
                    alertDialogManager.showAlertDialog(
                            User_Main_Activity.this,
                            "NOTION",
                            "Please complete your personal profile first.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Người dùng ấn đồng ý
                                    // Tạo Intent để khởi tạo lại Activity

                                    Intent intent = getIntent();
                                    finish(); // Kết thúc Activity hiện tại
                                    startActivity(intent); // Khởi tạo lại Activity
                                    selectedFragment[0] = User;
                                }
                            }
                    );

                }
                else{
                    Intent intent = new Intent(User_Main_Activity.this, PTrainer_Update_Level_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }


        }else if(item.getItemId() == R.id.setting){
            selectedFragment[0] = new User_Setting_Fragment();

        }else if(item.getItemId() == R.id.feedbackApp){
            selectedFragment[0] = new User_Rate_App_Fragment();

        }else if(item.getItemId() == R.id.follow_us){
            Intent intent = new Intent(User_Main_Activity.this, User_Follow_Us_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

        replaceFagment(selectedFragment[0]);
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
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, fragment);
            transaction.commit();
        } else {
            // Log một lỗi hoặc xử lý trường hợp khi fragment là null
            Log.e("User_Main_Activity", "Attempted to replace fragment with null");
        }
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
                        String Bmi = resultSet.getString("BMI");
                        String level = resultSet.getString("Level");
                        userModel = new UserModel(id, Name, Email , Phone, Bmi , Integer.parseInt(level));
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
                    bmii = result.getBMI();

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