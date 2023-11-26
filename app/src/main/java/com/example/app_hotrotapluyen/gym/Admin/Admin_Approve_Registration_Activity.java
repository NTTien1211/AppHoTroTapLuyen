package com.example.app_hotrotapluyen.gym.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.login_regis.LoginActivity;
import com.example.app_hotrotapluyen.gym.notification.ApiClient;
import com.example.app_hotrotapluyen.gym.notification.ApiService;
import com.example.app_hotrotapluyen.gym.notification.NotificationMessaging;
import com.example.app_hotrotapluyen.gym.notification.Response;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;

public class Admin_Approve_Registration_Activity extends AppCompatActivity {
    ImageView profile_pic_image_view_userupdate_apporve,User_cretificate_update_pt_inormation_img_apporve,
            User_prize_update_pt_inormation_img1_apporve,User_prize_update_pt_inormation_img2_apporve,
            User_prize_update_pt_inormation_img3_apporve;
    TextView User_user_inormation_update_apporve, User_Name_exper_update_pt_inormation_apporve,
            User_Gender_exper_update_pt_inormation_apporve,User_phone_userUpdate_inormation_apporve,
            User_email_userUpdate_inormation_apporve,User_BMI_userUpdate_inormation_apprpve,
            User_prize_update_pt_inormation1_apporve,User_prize_update_pt_inormation2_apporve,
            User_prize_update_pt_inormation3_apporve;
    String idUser ;
    Button btn_update_userPT_profile_Accept, btn_update_userPT_profile_Deny;
    UserModel userModel = null ;
    List<String> prizeNameList = new ArrayList<>();
    String myString;
    List<String> prizeImgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve_registration);
        idUser =  getIntent().getExtras().getString("PT_ID");
        anhxa();
        Toolbar actionBar = findViewById(R.id.toolbar_userupdate);
        setToolbar(actionBar, "Approve Registration PT");
        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();
        btn_update_userPT_profile_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetUserUpdate getUserUpdate = new GetUserUpdate();
                getUserUpdate.execute();
                onBackPressed();
            }
        });
        btn_update_userPT_profile_Deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DenyUserUpdate denyUserUpdate = new DenyUserUpdate();
                denyUserUpdate.execute();
                Toast.makeText(Admin_Approve_Registration_Activity.this, "Deny level", Toast.LENGTH_SHORT).show();
                onBackPressed();
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

    private void anhxa() {
        profile_pic_image_view_userupdate_apporve = findViewById(R.id.profile_pic_image_view_userupdate_apporve);
        User_cretificate_update_pt_inormation_img_apporve= findViewById(R.id.User_cretificate_update_pt_inormation_img_apporve);
        User_prize_update_pt_inormation_img1_apporve= findViewById(R.id.User_prize_update_pt_inormation_img1_apporve);
        User_prize_update_pt_inormation_img2_apporve= findViewById(R.id.User_prize_update_pt_inormation_img2_apporve);
        User_prize_update_pt_inormation_img3_apporve= findViewById(R.id.User_prize_update_pt_inormation_img3_apporve);


        User_user_inormation_update_apporve= findViewById(R.id.User_user_inormation_update_apporve);
        User_Name_exper_update_pt_inormation_apporve= findViewById(R.id.User_Name_exper_update_pt_inormation_apporve);
        User_Gender_exper_update_pt_inormation_apporve= findViewById(R.id.User_Gender_exper_update_pt_inormation_apporve);
        User_phone_userUpdate_inormation_apporve= findViewById(R.id.User_phone_userUpdate_inormation_apporve);
        User_email_userUpdate_inormation_apporve= findViewById(R.id.User_email_userUpdate_inormation_apporve);
        User_BMI_userUpdate_inormation_apprpve= findViewById(R.id.User_BMI_userUpdate_inormation_apprpve);
        User_prize_update_pt_inormation1_apporve= findViewById(R.id.User_prize_update_pt_inormation1_apporve);
        User_prize_update_pt_inormation2_apporve= findViewById(R.id.User_prize_update_pt_inormation2_apporve);
        User_prize_update_pt_inormation3_apporve= findViewById(R.id.User_prize_update_pt_inormation3_apporve);

        btn_update_userPT_profile_Accept= findViewById(R.id.btn_update_userPT_profile_Accept);
        btn_update_userPT_profile_Deny= findViewById(R.id.btn_update_userPT_profile_Deny);
    }
    private class DenyUserUpdate extends AsyncTask<Void, Void, UserModel> {
        @Override
        protected UserModel doInBackground(Void... voids) {
            Connection connection = JdbcConnect.connect();

            if (connection != null) {
                try {
                    // Update the user status to "deny"
                    String updateQuery = "UPDATE Users SET Status = 'deny' WHERE ID_User = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, idUser);
                    updateStatement.executeUpdate();

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
            return null; // You may return null or any other appropriate value
        }

    }

    private class GetUserUpdate extends AsyncTask<Void, Void, UserModel> {
        @Override
        protected UserModel doInBackground(Void... voids) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // Update the user level to 1
                    String updateQuery = "UPDATE Users SET Level = 1, Status ='' WHERE ID_User = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, idUser);
                    updateStatement.executeUpdate();

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
            return userModel; // You may return null or any other appropriate value
        }

        @Override
        protected void onPostExecute(UserModel userModel) {
            if (userModel != null){
                Toast.makeText(Admin_Approve_Registration_Activity.this, "Update level", Toast.LENGTH_SHORT).show();
               // String token = "f66WY2pwT1KIszzYiCa2bc:APA91bEQ0Q4exOlBj83pv5eNNxrZP6RsBVqLhdajIs83AmyhlXcmXRYegp2oNu7GLaaEOl6pU2wd2-ujN0FA-_RmNDF8pO4CN0CMYApiaR0YpkZhFnMT_t-fYUvdVk7Wh30FtD8i7XoO";
                String title = "Notification" , content = "Admin update level PT" ;
                String img = "string";
                Map<String, String> data = new HashMap<>();
                data.put("key1", "value1");
                data.put("key2", "value2");

                GetTokenByUser tokendiver = new GetTokenByUser(idUser);
                tokendiver.execute();

                try {
                    // Wait for the AsyncTask to finish (Note: This is a blocking operation and might cause ANR in the UI thread)
                    String token = tokendiver.get();

                    // Now you have the token, you can use it in another part of your code
                    if (token != null) {
                        // Use the token
                        // Example: Print the token
                        Log.d("TAG", "tokennnn: " + token);
                        NotificationRent(token, title, content, img, data);
                        System.out.println("Token: " + token);
                    } else {
                        // Handle the case where the token is null (AsyncTask failed)
                        System.out.println("Failed to retrieve token.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve the interrupt status
                    e.printStackTrace();
                    // Handle InterruptedException
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    // Handle ExecutionException
                }


            }
            else {
                Toast.makeText(Admin_Approve_Registration_Activity.this, "Fail level", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void NotificationRent(String token, String title, String body, String img, Map<String,String> data){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        NotificationMessaging notificationMessaging = new NotificationMessaging(token,title,body,img,data);
        Call<Response> call = apiService.sendNotification(notificationMessaging);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    Response responseAll = response.body();
                    Toast.makeText(Admin_Approve_Registration_Activity.this, "" + responseAll, Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onResponse Noti: " + responseAll.getMessage());
                } else {
                    Log.e("TAG", "onResponse Notielse: " + response.code());
                    Log.e("TAG", "onResponse Notifition: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(Admin_Approve_Registration_Activity.this, "Lỗi kết nối mạng hoặc máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Api calling: ",t);
            }
        });
    }
    public class GetTokenByUser extends AsyncTask<Void, Void, String> {
        private String idUser;
        private String token;

        // Constructor để nhận ID_User
        public GetTokenByUser(String idUser) {
            this.idUser = idUser;
        }

        @Override
        protected String doInBackground(Void... params) {
            Connection connection = JdbcConnect.connect();

            if (connection != null) {
                try {
                    // Select Tokendevice from Users WHERE ID_User = ?
                    String selectQuery = "SELECT Tokendevice FROM Users WHERE ID_User = ?";
                    try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                        selectStatement.setString(1, idUser);

                        try (ResultSet resultSet = selectStatement.executeQuery()) {
                            if (resultSet.next()) {
                                token = resultSet.getString("Tokendevice");
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
            return token;
        }

        @Override
        protected void onPostExecute(String result) {
            // Do something with the result, e.g., assign it to a variable
            myString = result;
        }
    }

    private class GetUserTask extends AsyncTask<Void, Void, UserModel> {
        @Override
        protected UserModel doInBackground(Void... voids) {
            Connection connection = JdbcConnect.connect();

            if (connection != null) {
                try {
                    String query = "SELECT Users.*, Prize.Name AS PrizeName, Prize.Img AS PrizeImg " +
                            "FROM Users " +
                            "INNER JOIN Prize ON Users.ID_User = Prize.ID_User " +
                            "WHERE Users.ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idUser);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        if (userModel == null) {
                            // Retrieve User information (only once)
                            String name = resultSet.getString("Name");
                            String userId = resultSet.getString("ID_User");
                            String email = resultSet.getString("Email");
                            String weight = resultSet.getString("Weight");
                            String phone = resultSet.getString("Phone");
                            String height = resultSet.getString("Height");
                            String gender = resultSet.getString("Gender");
                            String certificate = resultSet.getString("cretificate");
                            String experience = resultSet.getString("Experience");
                            String BMI = resultSet.getString("BMI");
                            String img = resultSet.getString("IMG");

                            userModel = new UserModel(userId, name, phone, email, certificate, weight, height, gender, BMI, Integer.parseInt(experience), img);
                        }

                        // Retrieve Prize information
                        String prizeName = resultSet.getString("PrizeName");
                        String prizeImg = resultSet.getString("PrizeImg");

                        prizeNameList.add(prizeName);
                        prizeImgList.add(prizeImg);
                        // Do something with the retrieved data

                    }
;
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
                Picasso.get().load(a).transform(new CircleTransform()).into(profile_pic_image_view_userupdate_apporve);
                Picasso.get().load(success.getCretificate()).into(User_cretificate_update_pt_inormation_img_apporve);
                User_user_inormation_update_apporve.setText(success.getName());
                User_Gender_exper_update_pt_inormation_apporve.setText(success.getGender());
                User_Name_exper_update_pt_inormation_apporve.setText(String.valueOf(success.getExperience()) );
                User_phone_userUpdate_inormation_apporve.setText(success.getPhone());
                User_email_userUpdate_inormation_apporve.setText(success.getEmail());

                User_BMI_userUpdate_inormation_apprpve.setText(success.getBMI());
                String text =  success.getBMI();
                float bmi = Float.parseFloat(text.replace("," ,"."));
                if (bmi <= 18.5) {
                    User_BMI_userUpdate_inormation_apprpve.setBackgroundColor(getResources().getColor(R.color.lavender)); // Thay thế colorBlue bằng màu bạn muốn sử dụng
                } else if (bmi <= 24.9) {
                    User_BMI_userUpdate_inormation_apprpve.setBackgroundColor(getResources().getColor(R.color.bmi_overweight)); // Thay thế colorGreen bằng màu bạn muốn sử dụng
                } else if (bmi <= 29.9) {
                    User_BMI_userUpdate_inormation_apprpve.setBackgroundColor(getResources().getColor(R.color.bmi_obesity)); // Thay thế colorOrange bằng màu bạn muốn sử dụng
                } else if (bmi <= 34.9) {
                    User_BMI_userUpdate_inormation_apprpve.setBackgroundColor(getResources().getColor(R.color.bmi_underweight)); // Thay thế colorRed bằng màu bạn muốn sử dụng
                } else {
                    User_BMI_userUpdate_inormation_apprpve.setBackgroundColor(getResources().getColor(R.color.purple_dam)); // Thay thế colorPurple bằng màu bạn muốn sử dụng
                }
                String[] prizeNames = prizeNameList.toArray(new String[0]);
                String[] prizeImgs = prizeImgList.toArray(new String[0]);
                Log.d("TAG", "onPostExecute: " + prizeNames.length);
                Log.d("TAG", "onPostExecute: " + prizeImgs.length);

                if (prizeNames.length > 1 ) {
                    User_prize_update_pt_inormation1_apporve.setText(prizeNames[0]);
                    User_prize_update_pt_inormation2_apporve.setText(prizeNames[1]);
                    User_prize_update_pt_inormation3_apporve.setText(prizeNames[2]);
                } else {
                    // Xử lý khi mảng không có đủ phần tử
                }
                if (prizeImgs.length >1 ){
                    Picasso.get().load(prizeImgs[0]).into(User_prize_update_pt_inormation_img1_apporve);
                    Picasso.get().load(prizeImgs[1]).into(User_prize_update_pt_inormation_img2_apporve);
                    Picasso.get().load(prizeImgs[2]).into(User_prize_update_pt_inormation_img3_apporve);
                }
                else{

                }


                // Display prize images in ImageViews using Picasso



            } else {
                // Handle case when user data is not found
            }
        }
    }
}