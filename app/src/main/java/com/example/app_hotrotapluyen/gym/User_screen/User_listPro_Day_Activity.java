package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.ProGram_Day_User_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_UserPR_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.DayPRo_model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    String level;
    FloatingActionButton add_programDay_pt;
    List<DayPRo_model> DAYMODEL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_pro_day);
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idPro = sharedPreferences.getInt("ID_Pro",0);
        level = sharedPreferences.getString("levelID","");
        recycle_lispro_day_user = findViewById(R.id.recycle_lispro_day_user);
        add_programDay_pt = findViewById(R.id.add_program_pt_day);
        Toolbar actionBar = findViewById(R.id.toolbar);
        setToolbar(actionBar, "Day");
        DAYMODEL = new ArrayList<>();
        SelecDatabase selecDatabase1 = new SelecDatabase();
        selecDatabase1.execute();
        if (Integer.parseInt(level)  == 1){
            add_programDay_pt.setVisibility(View.VISIBLE);
            add_programDay_pt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hiển thị dialog thêm name và level
                    showAddDialog();
                }
            });

        }else {
            add_programDay_pt.setVisibility(View.GONE);
        }

    }
    private void showAddDialog() {
        // Sử dụng AlertDialog.Builder để tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add program");


        // Inflate layout cho dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.ptrainer_add_day, null);
        builder.setView(dialogView);
//        dialogView.setBackgroundResource(R.drawable.rounded_background2);

        // Ánh xạ các thành phần trong dialog
        final Spinner levelSpinner = dialogView.findViewById(R.id.levelSpinner_Day);

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.day_array, android.R.layout.simple_spinner_item);

        // Đặt layout cho dropdown của Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Đặt ArrayAdapter vào Spinner
        levelSpinner.setAdapter(adapter);

        // Xử lý sự kiện khi người dùng nhấn nút "OK"
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy giá trị từ EditText và Spinner
                String selectedLevel = levelSpinner.getSelectedItem().toString();
                DayPRo_model programModel = new DayPRo_model();
                programModel.setNameDay(selectedLevel);
                new AddDayToDatabase().execute(programModel);
                // Thực hiện các hành động với name và selectedLevel ở đây

                // Đóng dialog
                dialog.dismiss();
                recreate();
            }
        });

        // Xử lý sự kiện khi người dùng nhấn nút "Hủy"
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog
                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private class AddDayToDatabase extends AsyncTask<DayPRo_model, Void, Void> {

        @Override
        protected Void doInBackground(DayPRo_model... days) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // Lấy ngày từ tham số
                    DayPRo_model day = days[0];

                    // Kiểm tra xem ngày đã tồn tại cho ID_Pro chưa
                    if (dayExists(connection, day)) {
                        // Nếu tồn tại, thực hiện update
                        updateDay(connection, day);
                    } else {
                        // Nếu chưa tồn tại, thực hiện insert
                        insertDay(connection, day);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (java.sql.SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return null;
        }

        // Kiểm tra xem ngày đã tồn tại cho ID_Pro chưa
        private boolean dayExists(Connection connection, DayPRo_model day) throws SQLException {
            String selectQuery = "SELECT COUNT(*) FROM Day WHERE ID_Pro = ? AND Day = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setLong(1, idPro);
                preparedStatement.setString(2, day.getNameDay());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
            }
            return false;
        }

        // Thực hiện update ngày
        private void updateDay(Connection connection, DayPRo_model day) throws SQLException {
            String updateQuery = "UPDATE Day SET Day = ? WHERE ID_Pro = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, day.getNameDay());
                preparedStatement.setLong(2,idPro);
                preparedStatement.executeUpdate();
            } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // Thực hiện insert ngày mới
        private void insertDay(Connection connection, DayPRo_model day) throws SQLException {
            String insertQuery = "INSERT INTO Day (Day, ID_Pro) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, day.getNameDay());
                preparedStatement.setLong(2, idPro);
                preparedStatement.executeUpdate();
            } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
            }
        }
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