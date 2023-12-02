package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.PTrainer.PTrainer_Update_Level_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Grid_Home_UserPT_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class User_PT_Inf_Activity extends AppCompatActivity {
    ImageButton back;
    TextView User_pt_inormation ,User_Manager_pt_inormation , User_year_pt_inormation,User_rate_pt_inormation
            ,Phone_ptIn_User ,List_ptIn_money ,List_ptIn_prize;
    String idPT;
    ImageView User_pt_rate ,profile_pic_image_view_cPTinf ,img_dgree;
    UserModel userList ;
    Button  Btn_Pt_User_Book;
    String idUser,level;
    LinearLayout starLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pt_inf);
        Toolbar actionBar = findViewById(R.id.toolbar_proIn4);
        setToolbar(actionBar, "PTrainer Information");
        idPT = getIntent().getExtras().getString("PT_ID");
        Toast.makeText(this, "" +idPT, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        level = sharedPreferences.getString("levelID" ,"");
        anhxa();
        if (Integer.parseInt(level) == 0){
            Btn_Pt_User_Book.setVisibility(View.VISIBLE);
        }
        else {
            Btn_Pt_User_Book.setVisibility(View.GONE);
        }
        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute(idPT);

        Btn_Pt_User_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = List_ptIn_money.getText().toString();
                Intent intent = new Intent(User_PT_Inf_Activity.this, User_Book_pt_time_Activity.class);
                intent.putExtra("idPtss" , idPT);
                intent.putExtra("moneybook" , money);
                startActivity(intent);
            }
        });
    }

    private void anhxa() {
        User_pt_inormation = findViewById(R.id.User_pt_inormation);
        User_Manager_pt_inormation = findViewById(R.id.User_Manager_pt_inormation);
        User_year_pt_inormation = findViewById(R.id.User_year_pt_inormation);
        Phone_ptIn_User = findViewById(R.id.Phone_ptIn_User);
        User_rate_pt_inormation = findViewById(R.id.User_rate_pt_inormation);
        List_ptIn_money = findViewById(R.id.List_ptIn_money);
        List_ptIn_prize = findViewById(R.id.List_ptIn_prize);
        img_dgree = findViewById(R.id.img_dgree);
        Btn_Pt_User_Book = findViewById(R.id.Btn_Pt_User_Book);
//        User_pt_rate = findViewById(R.id.User_pt_rate);
        starLayout = findViewById(R.id.User_pt_layout_rate);
        profile_pic_image_view_cPTinf = findViewById(R.id.profile_pic_image_view_cPTinf);
        starLayout.setGravity(Gravity.CENTER);
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

    private class SelecDatabase extends AsyncTask<String, Void, UserModel> {

        @Override
        protected UserModel  doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {

                    String query = "SELECT * FROM Users WHERE ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(idPT));

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        String name = resultSet.getString("Name");
                        String Userid = resultSet.getString("ID_User");
                        String Dregree = resultSet.getString("cretificate");

                        String phone = resultSet.getString("Phone");
                        String Evaluate = resultSet.getString("Evaluate");
                        Float rate = resultSet.getFloat("Rate");
                        DecimalFormat decimalFormat = new DecimalFormat("#,#");
                        float rateFormat = Float.parseFloat(decimalFormat.format(rate));
                        int people = resultSet.getInt("People");
                        int Experience = resultSet.getInt("Experience");
                        String img = resultSet.getString("IMG");
                        int money = resultSet.getInt("Money");


                        userList = new UserModel(Userid , name,phone,Dregree,"prize", Evaluate, money ,Experience, people , rateFormat,img);
                        return userList;
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
            return  userList ;
        }


        @Override
        protected void onPostExecute(UserModel userList) {
            super.onPostExecute(userList);
            if (userList != null ) {
                User_pt_inormation.setText(userList.getName());
                User_Manager_pt_inormation.setText(String.valueOf(userList.getManagers()));
                User_year_pt_inormation.setText(String.valueOf(userList.getExperience()));
                Phone_ptIn_User.setText(userList.getPhone());
                User_rate_pt_inormation.setText(String.valueOf(userList.getRate()));
                List_ptIn_money.setText(String.valueOf(userList.getMoney()));
                Picasso.get().load(userList.getCretificate()).into(img_dgree);

                List_ptIn_prize.setText(userList.getPrize());
                String img = userList.getImg();
                if (img == null || img.isEmpty()){
                    profile_pic_image_view_cPTinf.setImageDrawable(getResources().getDrawable(R.drawable.person_icon));
                }
                else {
                    Picasso.get().load(img).transform(new CircleTransform()).into(profile_pic_image_view_cPTinf);
                }
                for (int i = 0; i < userList.getRate(); i++) {
                    ImageView starImageView = new ImageView(User_PT_Inf_Activity.this); // Chú ý vào this
                    starImageView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT

                    ));
                    starImageView.setImageResource(R.drawable.baseline_star_24);

                    // Thêm ImageView vào layout cha
                    starLayout.addView(starImageView);
                }
            } else {
                Toast.makeText(User_PT_Inf_Activity.this, "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }



}