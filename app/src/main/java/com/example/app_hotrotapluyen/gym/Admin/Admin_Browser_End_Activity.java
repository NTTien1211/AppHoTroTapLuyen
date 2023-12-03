package com.example.app_hotrotapluyen.gym.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.Admin.Adapter.Admin_Over_Br_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.BookModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_feedback_rate_Activity;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Admin_Browser_End_Activity extends AppCompatActivity {
    Toolbar toolbar_broser;
    TextView name_user_admin_brower_PT , name_user_admin_brower_phone_PT;
    TextView name_user_admin_brower_user , name_user_admin_brower_phone_user ,TimeMonth_book_ptIn_User ,time_inday_book;
    TextView Time_book_ptIn_User ,Money_book_ptIn_User;
    Button btn_admin_broser_check ,btn_user_feedback_check;
    ImageView admin_broser_imgPT , admin_broser_imgUS;
    Long idBook;
    String level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_browser_end);
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        level = sharedPreferences.getString("levelID" ,"");
        annxa();
        setToolbar(toolbar_broser, "");
        idBook = getIntent().getExtras().getLong("ID_Book_ts");
        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute();
        if (Integer.parseInt(level) == 2){
            btn_user_feedback_check.setVisibility(View.GONE);
            btn_admin_broser_check.setVisibility(View.VISIBLE);
            btn_admin_broser_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AcceptRequest().execute();
                    onBackPressed();
                }
            });
        }else  if (Integer.parseInt(level) == 0 || Integer.parseInt(level) == 1) {
            btn_user_feedback_check.setVisibility(View.VISIBLE);
            btn_admin_broser_check.setVisibility(View.GONE);
            btn_user_feedback_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Admin_Browser_End_Activity.this , User_feedback_rate_Activity.class);
                    startActivity(intent);
                }
            });
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
    private class SelecDatabase extends AsyncTask<String, Void, List<BookModel>> {
        List<BookModel> userList = new ArrayList<>();

        @Override
        protected List<BookModel> doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Book.ID_Book, Book.ID_User_Give, Book.Time as Time, Book.Money as Money, Book.Status,Book.timein_day as Time_inday,Book.duration as Duration," +
                            "Book.ID_User, Users1.Name AS UserName, Users1.IMG AS UserIMG, Users1.Phone AS UserPhone, " +
                            "Users1.Email AS UserEmail, Users2.Name AS GiverName, Users2.IMG AS GiverIMG, " +
                            "Users2.Phone AS GiverPhone, Users2.Email AS GiverEmail " +
                            "FROM Book " +
                            "INNER JOIN Users AS Users1 ON Book.ID_User = Users1.ID_User " +
                            "INNER JOIN Users AS Users2 ON Book.ID_User_Give = Users2.ID_User " +
                            "WHERE Book.ID_Book = ?";  // Sử dụng tham số để tránh SQL Injection

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setLong(1, idBook);  // Gán giá trị cho tham số
                    ResultSet resultSet = preparedStatement.executeQuery();


                    while (resultSet.next()) {
                        Long id = resultSet.getLong("ID_Book");
                        Long userPTId = resultSet.getLong("ID_User_Give");
                        String giverName = resultSet.getString("GiverName");
                        String giverImg = resultSet.getString("GiverIMG");
                        String giverPhone = resultSet.getString("GiverPhone");
                        String giverEmail = resultSet.getString("GiverEmail");
                        UserModel userPT = new UserModel(String.valueOf(userPTId), giverName, giverPhone, giverEmail, giverImg);

                        Timestamp time = resultSet.getTimestamp("Time");
                        double money = resultSet.getDouble("Money");
                        String status = resultSet.getString("Status");
                        int duration = resultSet.getInt("Duration");
                        String Time_inday = resultSet.getString("Time_inday");

                        Long userUSId = resultSet.getLong("ID_User");
                        String userName = resultSet.getString("UserName");
                        String userImg = resultSet.getString("UserIMG");
                        String userPhone = resultSet.getString("UserPhone");
                        String userEmail = resultSet.getString("UserEmail");
                        UserModel userUS = new UserModel(String.valueOf(userUSId), userName, userPhone, userEmail, userImg);

                        BookModel bookModel = new BookModel(id, userPT, time, money, status, userUS,duration,Time_inday);
                        userList.add(bookModel);


                }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "BookingTTTTTTT: " + e);
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e("TAG", "BookingTTTTTTT222222: " + e);
                    }
                }
            }
            return userList;
        }

        @Override
        protected void onPostExecute(List<BookModel> userList) {
            super.onPostExecute(userList);
            if (userList != null && userList.size() > 0) {
                BookModel firstBook = userList.get(0);
                UserModel userGive = firstBook.getUser_Give();
                UserModel userUS = firstBook.getUsers();

                if (userGive != null && userUS != null) {
                    // Set text for PT (userGive)
                    name_user_admin_brower_PT.setText(userGive.getName());
                    name_user_admin_brower_phone_PT.setText(userGive.getPhone());
                    Picasso.get().load(userGive.getImg()).into(admin_broser_imgPT);
                    SharedPreferences sharedPreferences = Admin_Browser_End_Activity.this.getSharedPreferences("GymTien", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id_people_give", String.valueOf(userGive.getIdUser()));
                    editor.apply();
                    // Set text for User (userUS)
                    name_user_admin_brower_user.setText(userUS.getName());
                    name_user_admin_brower_phone_user.setText(userUS.getPhone());
                    Picasso.get().load(userUS.getImg()).into(admin_broser_imgUS);

                    // Set text for Time and Money
                    Time_book_ptIn_User.setText(String.valueOf(firstBook.getTimeDay()));
                    TimeMonth_book_ptIn_User.setText(String.valueOf(firstBook.getDuration()));
                    time_inday_book.setText(String.valueOf(firstBook.getTimein_day()));
                    Money_book_ptIn_User.setText(String.valueOf(firstBook.getMoney()));
                } else {
                    // Handle the case where either userGive or userUS is null
                    Log.e("TAG", "User_Give or User_US is null");
                }

            } else {
                Toast.makeText(Admin_Browser_End_Activity.this, "FAIL", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class  AcceptRequest extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // Cập nhật trạng thái (status) của đơn hàng có ID bằng idBook thành 'accept'
                    String updateQuery = "UPDATE Book SET Status = 'accept' WHERE ID_Book = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setLong(1, idBook);  // Gán giá trị cho tham số
                    int rowsAffected = updateStatement.executeUpdate();

                    // Kiểm tra xem có bản ghi nào bị ảnh hưởng không
                    return rowsAffected > 0;
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "AcceptRequest: " + e);
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e("TAG", "AcceptRequest: " + e);
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                // Xử lý khi cập nhật thành công, có thể hiển thị thông báo hoặc thực hiện các hành động khác
                Toast.makeText(Admin_Browser_End_Activity.this, "Request accepted", Toast.LENGTH_SHORT).show();
            } else {
                // Xử lý khi cập nhật không thành công
                Toast.makeText(Admin_Browser_End_Activity.this, "Failed to accept request", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void annxa() {
        toolbar_broser = findViewById(R.id.toolbar_broser);
        name_user_admin_brower_PT = findViewById(R.id.name_user_admin_brower_PT);
        name_user_admin_brower_phone_PT = findViewById(R.id.name_user_admin_brower_phone_PT);
        name_user_admin_brower_user = findViewById(R.id.name_user_admin_brower_user);
        name_user_admin_brower_phone_user = findViewById(R.id.name_user_admin_brower_phone_user);
        Time_book_ptIn_User = findViewById(R.id.Time_book_ptIn_User);
        Money_book_ptIn_User = findViewById(R.id.Money_book_ptIn_User);
        btn_admin_broser_check = findViewById(R.id.btn_admin_broser_check);
        admin_broser_imgUS = findViewById(R.id.admin_broser_imgUS);
        admin_broser_imgPT = findViewById(R.id.admin_broser_imgPT);
        btn_user_feedback_check = findViewById(R.id.btn_user_feedback_check);
        TimeMonth_book_ptIn_User = findViewById(R.id.TimeMonth_book_ptIn_User);
        time_inday_book = findViewById(R.id.time_inday_book);

    }

}