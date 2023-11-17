package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.ProGram_Day_User_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_UserPR_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.DayPRo_model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class User_listPro_Day_Activity extends AppCompatActivity {
    ProGram_Day_User_Adapter proGramDayUserAdapter;
    RecyclerView recycle_lispro_day_user;
    long idPro;
    List<DayPRo_model> DAYMODEL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_pro_day);
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idPro = sharedPreferences.getInt("ID_Pro",0);
        recycle_lispro_day_user = findViewById(R.id.recycle_lispro_day_user);
        Toolbar actionBar = findViewById(R.id.toolbar);
        setToolbar(actionBar, "Day");
        DAYMODEL = new ArrayList<>();
        SelecDatabase selecDatabase1 = new SelecDatabase();
        selecDatabase1.execute();

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
    private class  SelecDatabase extends AsyncTask<String, Void, List<DayPRo_model>> {

        @Override
        protected List<DayPRo_model> doInBackground(String... strings) {
             DAYMODEL = new ArrayList<>();
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Day.ID_Day as IDDAY,  Program.Name AS ProgramName, Day.Day AS DayName " +
                            "FROM Program " +
                            "JOIN Day ON Program.ID_Pro = Day.ID_Pro " +
                            "WHERE Day.ID_Pro = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setLong(1,idPro );

                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int idday = Integer.parseInt( resultSet.getString("IDDAY"));
                        String name = resultSet.getString("DayName");
                        Log.d("TAG", "DayName: " + name);
                        DayPRo_model pt = new DayPRo_model( idday, name, 0);
                        DAYMODEL.add(pt);
                    }
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return DAYMODEL;
        }
        @Override
        protected void onPostExecute(List<DayPRo_model> dayModel) {
            super.onPostExecute(dayModel);
            if (dayModel != null && dayModel.size() > 0) {
                recycle_lispro_day_user.setLayoutManager(new LinearLayoutManager(User_listPro_Day_Activity.this,RecyclerView.VERTICAL,false));
                proGramDayUserAdapter = new ProGram_Day_User_Adapter(dayModel);
                recycle_lispro_day_user.setAdapter(proGramDayUserAdapter);
            } else {
                Toast.makeText(User_listPro_Day_Activity.this, "AAAAAA ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}