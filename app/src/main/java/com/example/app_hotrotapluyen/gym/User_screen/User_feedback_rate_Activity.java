package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
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
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class User_feedback_rate_Activity extends AppCompatActivity {
    private RatingBar ratingBar;
    private Button submitButton;
    String idUsser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback_rate);
        ratingBar = findViewById(R.id.ratingBar);
        Toolbar actionBar = findViewById(R.id.toobar_feedback);
        setToolbar(actionBar, "Rate Personal Trainer");
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        idUsser = sharedPreferences.getString("userID" ,"");
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
    public void submitRating(View view) {
        float rating = ratingBar.getRating();
        // Xử lý đánh giá, ví dụ: lưu vào cơ sở dữ liệu, gửi lên server, ...
        Toast.makeText(this, "Đã đánh giá " + rating + " sao", Toast.LENGTH_SHORT).show();
    }

    private class BookPTTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // Lấy thông tin người được book (ID_User_Give)
                    String idPT = strings[0];

                    // Lấy thông tin người dùng (ID_User)
                    String idUser = strings[1];
                    String information = strings[2];
                    double rate = Double.parseDouble(strings[3]);

                    // Lấy thời gian hiện tại
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                    // Thêm dữ liệu vào bảng Book
                    String query = "INSERT INTO Feedback (ID_User_Give,Time,Information,Rate, ID_User) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idPT);
                    preparedStatement.setTimestamp(2, currentTime);
                    preparedStatement.setString(3, information);
                    preparedStatement.setDouble(4, rate);
                    preparedStatement.setString(5, idUser);


                    // Đặt Status là "waiting" (hoặc giá trị khác tùy theo yêu cầu)

                    // Thực hiện truy vấn và cập nhật bảng Book
                    int rowsAffected = preparedStatement.executeUpdate();

                    // Trả về true nếu có ít nhất một hàng bị ảnh hưởng (truy vấn thành công)
                    return rowsAffected > 0;

                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "prinrrrr: " + e);
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Trả về false nếu có lỗi xảy ra hoặc không có hàng nào bị ảnh hưởng
            return false;
        }
    }
}