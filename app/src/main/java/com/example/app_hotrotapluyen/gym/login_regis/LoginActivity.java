package com.example.app_hotrotapluyen.gym.login_regis;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.gym.User_screen.Model.HomeU_pt;
import com.example.app_hotrotapluyen.gym.notification.ApiClient;
import com.example.app_hotrotapluyen.gym.notification.ApiService;
import com.example.app_hotrotapluyen.gym.notification.NotificationMessaging;
import com.example.app_hotrotapluyen.gym.notification.Response;
import com.example.app_hotrotapluyen.gym.until.FirebaseUntil;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_Main_Activity;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView signupText;
    Button loginButton;
    UserModel userModel;
    public static final String ChannelID = "default_channel_id" ;
    HomeU_pt homeUPt;
    String nameFir, phonFir;
    Spinner spinner;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        until();
        click();
//        createChannelNotification();

    }

    private void click() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName = username.getText().toString();
                String PassWords = password.getText().toString();
                loginAndFetchUserDetails(UserName, PassWords);

            }
        });
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    public void loginAndFetchUserDetails(final String UserName, final String pass) {
        new AsyncTask<Void, Void, UserModel>() {
            @Override
            protected UserModel doInBackground(Void... params) {
                Connection connection = JdbcConnect.connect();
                if (connection != null) {
                    try {
                        String query = "SELECT ID_User, Name, Email, Pass, Phone, Level FROM Users WHERE (Email = ? OR Phone = ?) AND Pass = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, UserName);
                        preparedStatement.setString(2, UserName);
                        preparedStatement.setString(3, pass);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            String userId = resultSet.getString("ID_User");
                            String name = resultSet.getString("Name");
                            String phone = resultSet.getString("Phone");
                            int level = resultSet.getInt("Level");
                            userModel = new UserModel(userId, name, phone , level);
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
            protected void onPostExecute(UserModel userDetails) {
                if (userDetails != null) {
                    String nameFir = userDetails.getName();
                    String phonFir = userDetails.getPhone();
                    String level  = String.valueOf(userDetails.getLevel());
                    UserModel user = new UserModel(userDetails.getIdUser(), nameFir, phonFir,level);
                    setUsername(user);
                    SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userID",userDetails.getIdUser());
                    editor.putString( "levelID" ,String.valueOf(userDetails.getLevel()));
                    editor.apply();
                    UpdateFunsion(Long.parseLong(user.getIdUser()));
                    Intent intent = new Intent(LoginActivity.this, User_Main_Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Ktra lai thong tin đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void  UpdateFunsion(Long id){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        LoginUpdateToken(id ,token );
                    }
                });

    }
    public void LoginUpdateToken(  Long idUser,  String newTokendevice) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Connection connection = JdbcConnect.connect();
                if (connection != null) {
                    try {
                        String updateQuery = "UPDATE Users SET Tokendevice = ? WHERE ID_User = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                        preparedStatement.setString(1, newTokendevice); // Set the new token device value
                        preparedStatement.setLong(2, idUser); // Set the user ID for the update
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            // Update successful
                            System.out.println("Update successful.");
                        } else {
                            // No rows were updated, user not found or no changes made
                            System.out.println("User not found or no changes made.");
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
                return newTokendevice;
            }

            @Override
            protected void onPostExecute(String token) {
                if (!token.isEmpty()) {

                } else {
                    Toast.makeText(LoginActivity.this, "Ktra lai thong tin token", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @SuppressLint("WrongViewCast")
    private void until() {
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        signupText = findViewById(R.id.loginsignupText);
        loginButton = findViewById(R.id.loginButton);

    }


void setUsername(final UserModel user) {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {
        Log.d("TAG", "setUsername: " + currentUser);
        String userId = currentUser.getUid();
        String username = user.getName();
        if (userModel != null) {
            userModel.setName(username);
        } else {
            userModel = new UserModel(userId, user.getName(), user.getPhone(),1);
            FirebaseUntil.allUserCollectionReference()
                    .whereEqualTo("phone", user.getPhone())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            if (querySnapshot.isEmpty()) {
                                // Người dùng chưa tồn tại, thêm vào cơ sở dữ liệu
                                FirebaseUntil.allUserCollectionReference()
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                // Tài liệu đã được thêm thành công

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Firebase", "Lỗi khi thêm người dùng: " + e.getMessage());
                                            }
                                        });
                            } else {
                                Log.d("Firebase", "Người dùng đã tồn tại");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý lỗi khi kiểm tra người dùng tồn tại hay không
                            Log.e("Firebase", "Lỗi khi kiểm tra người dùng: " + e.getMessage());
                        }
                    });

        }
        }else {
        Log.d("TAG", "setUsername: " + currentUser);
    }
        // Kiểm tra xem người dùng đã tồn tại hay chưa bằng số điện thoại
}
    private void createChannelNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(ChannelID,"default_channel_id",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }

}