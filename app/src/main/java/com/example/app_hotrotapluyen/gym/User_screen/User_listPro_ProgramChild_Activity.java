package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.ProGram_Day_User_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_Day_User_Child_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_UserPR_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.DayPRo_model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.Program_child_Model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class User_listPro_ProgramChild_Activity extends AppCompatActivity {
    Program_Day_User_Child_Adapter proGramDayUserAdapter;
    RecyclerView recycle_lispro_day_user;
    TextView tNameday,tSlbtapChild,tTimeChild;
    long idDay ;
    Button start_chill_exce;
    String dayname = "";
    List<Program_child_Model> DAYMODEL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_pro_program_child);
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idDay = sharedPreferences.getLong("id_idDay",0);
        recycle_lispro_day_user = findViewById(R.id.recycle_lispro_day_user_child);
        tNameday = findViewById(R.id.tNameDayChild);
        tSlbtapChild = findViewById(R.id.tSlbtapChild);
        tTimeChild = findViewById(R.id.tTimeChild);
        start_chill_exce = findViewById(R.id.start_chill_exce);
        Toolbar actionBar = findViewById(R.id.toolbarChild);
        setToolbar(actionBar, "");
        DAYMODEL = new ArrayList<>();
        SelecDatabase selecDatabase1 = new SelecDatabase();
        selecDatabase1.execute();
        start_chill_exce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_listPro_ProgramChild_Activity.this, Pro_StartPlan_Activity.class);
                startActivity(intent);
            }
        });


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
    private class  SelecDatabase extends AsyncTask<String, Void, List<Program_child_Model>> {

        @Override
        protected List<Program_child_Model> doInBackground(String... strings) {
            DAYMODEL = new ArrayList<>();
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Day.Day AS DayName, Program_Child.Name AS ProgramChildName, Program_Child.Calo, Program_Child.Unit as ProgramChildUnit, Program_Child.Img " +
                            "FROM Day " +
                            "JOIN Program_Child ON Day.ID_Day = Program_Child.ID_Day " +
                            "JOIN Program ON Day.ID_Pro = Program.ID_Pro " +
                            "WHERE Day.ID_Day = ?";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setLong(1, idDay);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String name = resultSet.getString("ProgramChildName");
                        String unil = resultSet.getString("ProgramChildUnit");
                        dayname = resultSet.getString("DayName");
                        Program_child_Model pt = new Program_child_Model(name,  Long.valueOf(unil));
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
        protected void onPostExecute(List<Program_child_Model> program_child_Model) {
            super.onPostExecute(program_child_Model);
            if (program_child_Model != null && program_child_Model.size() > 0) {
                recycle_lispro_day_user.setLayoutManager(new LinearLayoutManager(User_listPro_ProgramChild_Activity.this,RecyclerView.VERTICAL,false));
                proGramDayUserAdapter = new Program_Day_User_Child_Adapter(program_child_Model);
                recycle_lispro_day_user.setAdapter(proGramDayUserAdapter);
                tNameday.setText(dayname);
                tSlbtapChild.setText(String.valueOf(program_child_Model.size()));

            } else {
                Toast.makeText(User_listPro_ProgramChild_Activity.this, "AAAAAA ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}