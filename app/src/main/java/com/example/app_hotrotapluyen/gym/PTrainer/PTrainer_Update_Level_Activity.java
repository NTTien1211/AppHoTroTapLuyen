package com.example.app_hotrotapluyen.gym.PTrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_Repair_Inf_Activity;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.jdbcConnect.MediaManagerInitializer;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PTrainer_Update_Level_Activity extends AppCompatActivity {
    Toolbar actionBar;
    UserModel userModel;
    String idUser;
    String imgUrl;
    Button btn_update_userPT_profile;
    private static boolean isMediaManagerInitialized = false;
//    lấy ra
    ImageView profile_pic_image_view_userupdate ;
    TextView User_user_inormation_update,User_phone_userUpdate_inormation , User_email_userUpdate_inormation ,
            User_BMI_userUpdate_inormation;
//    update
    EditText User_Name_exper_update_pt_inormation ,User_prize_update_pt_inormation1 ,User_prize_update_pt_inormation2,
        User_prize_update_pt_inormation3 ,User_Name_money_update_pt_inormation;
    ImageView User_cretificate_update_pt_inormation_img ,User_prize_update_pt_inormation_img1,User_prize_update_pt_inormation_img2
            ,User_prize_update_pt_inormation_img3;
    private String[] imageUrls ; // Mảng để lưu trữ 3 địa chỉ ảnh
    private ImageView[] imageViews;
    String[] name;
    String cere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptrainer_update_level);
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        MediaManagerInitializer.initializeMediaManager(this);


        imageUrls = new String[4];
        imageViews = new ImageView[4];
        anhxa();
        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();
        setToolbar(actionBar, "UPDATE PTRAINER");
        name= new String[3];
        name[0] = User_prize_update_pt_inormation1.getText().toString();
        name[1] = User_prize_update_pt_inormation2.getText().toString();
        name[2] = User_prize_update_pt_inormation3.getText().toString();
        imageViews[0] = findViewById(R.id.User_cretificate_update_pt_inormation_img);
        imageViews[1] = findViewById(R.id.User_prize_update_pt_inormation_img1);
        imageViews[2] = findViewById(R.id.User_prize_update_pt_inormation_img2);
        imageViews[3] = findViewById(R.id.User_prize_update_pt_inormation_img3);
         cere = imageUrls[0];
        btn_update_userPT_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RegisterUserTask().execute(User_Name_exper_update_pt_inormation.getText().toString(), cere,
                        User_Name_money_update_pt_inormation.getText().toString(),
                        Arrays.toString(Arrays.asList(name).toArray(new String[0])),
                        Arrays.toString(Arrays.asList(imageUrls).toArray(new String[0])));
            }
        });
        for (int i = 0; i < imageViews.length; i++) {
            final int index = i;  // Để đảm bảo sự hiểu quả của biến index trong hàm onClick

            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickImage(index);  // Truyền index để xác định ImageView được click
                }
            });
        }

// Hàm chọn ảnh

    }

    private void pickImage(int index) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        // Sử dụng requestCode để xác định ImageView được click trong onActivityResult
        startActivityForResult(intent, index + 1);
    }
    private void anhxa() {
        actionBar = findViewById(R.id.toolbar_userupdate);
        btn_update_userPT_profile = findViewById(R.id.btn_update_userPT_profile);
//        lấy ra
        profile_pic_image_view_userupdate=findViewById(R.id.profile_pic_image_view_userupdate);
        User_user_inormation_update=findViewById(R.id.User_user_inormation_update);
        User_phone_userUpdate_inormation=findViewById(R.id.User_phone_userUpdate_inormation);
        User_email_userUpdate_inormation=findViewById(R.id.User_email_userUpdate_inormation);
        User_BMI_userUpdate_inormation=findViewById(R.id.User_BMI_userUpdate_inormation);

//        thêm vào
        User_Name_exper_update_pt_inormation=findViewById(R.id.User_Name_exper_update_pt_inormation);
        User_prize_update_pt_inormation1=findViewById(R.id.User_prize_update_pt_inormation1);
        User_prize_update_pt_inormation2=findViewById(R.id.User_prize_update_pt_inormation2);
        User_prize_update_pt_inormation3=findViewById(R.id.User_prize_update_pt_inormation3);

        User_cretificate_update_pt_inormation_img=findViewById(R.id.User_cretificate_update_pt_inormation_img);
        User_prize_update_pt_inormation_img1=findViewById(R.id.User_prize_update_pt_inormation_img1);
        User_prize_update_pt_inormation_img2=findViewById(R.id.User_prize_update_pt_inormation_img2);
        User_prize_update_pt_inormation_img3=findViewById(R.id.User_prize_update_pt_inormation_img3);
        User_Name_money_update_pt_inormation=findViewById(R.id.User_Name_money_update_pt_inormation);


    }

    @Override
    public void onBackPressed() {
        // Gửi broadcast để thông báo cho activity1 là cần làm mới
        Intent intent = new Intent("refresh_activity1_event");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        // Đảm bảo rằng bạn vẫn thực hiện việc đóng activity2
        super.onBackPressed();
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

    private class RegisterUserTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String exper = params[0];
                    String cretificate = params[1];
                    int money = Integer.parseInt(params[2]);
                    String[] names = params[3].substring(1, params[3].length() - 1).split(", ");
                    String[] imgs = params[4].substring(1, params[4].length() - 1).split(", ");

                    String query = "SELECT * FROM Users WHERE ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idUser);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String updateUserQuery = "UPDATE Users SET Experience=?, cretificate=?,Money=?, Status=? WHERE ID_User=?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateUserQuery);
                        updateStatement.setString(1, exper);
                        updateStatement.setString(2, imgs[0]);
                        updateStatement.setInt(3, money);
                        updateStatement.setString(4,"Waiting");
                        updateStatement.setString(5, idUser); // ID_User để xác định dòng cần cập nhật
                        int rowsUpdatedUser = updateStatement.executeUpdate();

                        // Update Prize table
                        String insertPrizeQuery = "INSERT INTO Prize (Name, Img, ID_User) VALUES (?, ?, ?)";
                        PreparedStatement insertPrizeStatement = connection.prepareStatement(insertPrizeQuery);

                        for (int i = 0; i < 3; i++) {
                            insertPrizeStatement.setString(1, names[i]);
                            insertPrizeStatement.setString(2, imgs[i+1]);
                            insertPrizeStatement.setString(3, idUser);

                            int rowsInserted = insertPrizeStatement.executeUpdate();

                            if (rowsInserted <= 0) {
                                // Handle insert failure for a specific row if needed
                            }
                        }


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
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(PTrainer_Update_Level_Activity.this, "Not accept", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(PTrainer_Update_Level_Activity.this, "Waiting Admin", Toast.LENGTH_SHORT).show();
                onBackPressed();

            }
        }
    }



    private class GetUserTask extends AsyncTask<Void, Void, UserModel> {
        @Override
        protected UserModel doInBackground(Void... voids) {
            Connection connection = JdbcConnect.connect();

            if (connection != null) {
                try {
                    String query = "SELECT * FROM Users WHERE ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idUser);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String name = resultSet.getString("Name");
                        String Userid = resultSet.getString("ID_User");
                        String email = resultSet.getString("Email");
                        String weight = resultSet.getString("Weight");
                        String phone = resultSet.getString("Phone");
                        String hight = resultSet.getString("Height");
                        String gender = resultSet.getString("Gender");
                        String BMI = resultSet.getString("BMI");
                        String Money = resultSet.getString("Money");
                        String img = resultSet.getString("IMG");
                        int in =  (Money != null) ? Integer.valueOf(Money) : 0;
                        userModel = new UserModel(Userid , name,phone,email,weight, hight ,gender,BMI, in ,  img);
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
        protected void onPostExecute(UserModel success) {
            if (success != null) {
                // Update UI with retrieved data
                String a =success.getImg();
                Log.d("TAG", "onPostExec22ute: " +a);
                 Picasso.get().load(a).transform(new CircleTransform()).into(profile_pic_image_view_userupdate);
                 User_user_inormation_update.setText(success.getName());
                 User_phone_userUpdate_inormation.setText(success.getPhone());
                 User_email_userUpdate_inormation.setText(success.getEmail());
                 User_BMI_userUpdate_inormation.setText(success.getBMI());
                User_Name_money_update_pt_inormation.setText(String.valueOf(success.getMoney()));
                String text =  success.getBMI();
                float bmi = Float.parseFloat(text.replace("," ,"."));
                if (bmi <= 18.5) {
                    User_BMI_userUpdate_inormation.setBackgroundColor(getResources().getColor(R.color.lavender)); // Thay thế colorBlue bằng màu bạn muốn sử dụng
                } else if (bmi <= 24.9) {
                    User_BMI_userUpdate_inormation.setBackgroundColor(getResources().getColor(R.color.bmi_overweight)); // Thay thế colorGreen bằng màu bạn muốn sử dụng
                } else if (bmi <= 29.9) {
                    User_BMI_userUpdate_inormation.setBackgroundColor(getResources().getColor(R.color.bmi_obesity)); // Thay thế colorOrange bằng màu bạn muốn sử dụng
                } else if (bmi <= 34.9) {
                    User_BMI_userUpdate_inormation.setBackgroundColor(getResources().getColor(R.color.bmi_underweight)); // Thay thế colorRed bằng màu bạn muốn sử dụng
                } else {
                    User_BMI_userUpdate_inormation.setBackgroundColor(getResources().getColor(R.color.purple_dam)); // Thay thế colorPurple bằng màu bạn muốn sử dụng
                }

            } else {
                // Handle case when user data is not found
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode <= 4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Tải ảnh lên Clouddinary
            try {
                uploadImageToCloudinary(imageUri, requestCode - 1); // Subtract 1 to get the correct index
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PTrainer_Update_Level_Activity.this, "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToCloudinary(Uri imageUri , int index) throws IOException {
        // Chỉ định các tùy chọn cho việc tải lên
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", "android_upload_" + System.currentTimeMillis()); // Đảm bảo mỗi lần tải lên là duy nhất

        // Thực hiện tải lên ảnh
        MediaManager.get().upload(imageUri)
                .option("tags", "android_upload")
                .option("upload_preset", "ml_default") // Thay thế bằng upload preset thực tế của bạn
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(PTrainer_Update_Level_Activity.this, "Đang tải lên...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Xử lý sự thay đổi trong quá trình tải lên
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imgUrl = (String) resultData.get("secure_url");
                        imageUrls[index] = imgUrl;

                        // Gắn địa chỉ ảnh vào ImageView tương ứng
                        Picasso.get().load(imgUrl).transform(new CircleTransform()).into(imageViews[index]);
                        Toast.makeText(PTrainer_Update_Level_Activity.this, "Tải lên thành công!" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(PTrainer_Update_Level_Activity.this, "Tải lên thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Xử lý khi cần lên lịch lại việc tải lên
                    }
                })
                .option("folder", "android_upload")
                .dispatch();
    }
}