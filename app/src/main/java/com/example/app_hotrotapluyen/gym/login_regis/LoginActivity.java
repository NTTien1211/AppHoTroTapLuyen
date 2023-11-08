package com.example.app_hotrotapluyen.gym.login_regis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.app_hotrotapluyen.gym.until.FirebaseUntil;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.User;
import com.example.app_hotrotapluyen.gym.User_screen.User_Main_Activity;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import kotlin.text.UStringsKt;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView signupText;
    Button loginButton;
    User user;
    String nameFir, phonFir;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getUsername();


        until();
        click();


    }

    private void click() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (username.getText().toString().equals("user") && password.getText().toString().equals("1234")) {
//                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
//                }
                String UserName = username.getText().toString();
                String PassWords = password.getText().toString();
//                setUsername();
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

    //    public String isLoginExists(final String UserName , final String pass ) {
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                Connection connection = JdbcConnect.connect();
//                if (connection != null) {
//                    try {
//                        String query = "SELECT ID_User,Name , Email, Pass , Phone FROM Users WHERE (Email = ? OR Phone = ?) AND Pass = ?";
//                        PreparedStatement preparedStatement = connection.prepareStatement(query);
//                        preparedStatement.setString(1, UserName);
//                        preparedStatement.setString(2, UserName);
//                        preparedStatement.setString(3, pass);
//                        ResultSet resultSet = preparedStatement.executeQuery();
//                        if (resultSet.next()) {
//                           return resultSet.getString("ID_User");
//
//                        }
//
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    } finally {
//                        try {
//                            connection.close();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String resultUser) {
//                if (resultUser!=null) {
//                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
////                    saveUserToFirestore(resultUser);
//                    Intent intent = new Intent(LoginActivity.this , User_Main_Activity.class);
//                    intent.putExtra("IdUser" , resultUser);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(LoginActivity.this, "Ktra lai thong tin", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//        return null; // Trả về giá trị mặc định, thay vì trả về từ AsyncTask
//    }
    public void loginAndFetchUserDetails(final String UserName, final String pass) {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... params) {
                Connection connection = JdbcConnect.connect();
                if (connection != null) {
                    try {
                        String query = "SELECT ID_User, Name, Email, Pass, Phone FROM Users WHERE (Email = ? OR Phone = ?) AND Pass = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, UserName);
                        preparedStatement.setString(2, UserName);
                        preparedStatement.setString(3, pass);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            String userId = resultSet.getString("ID_User");
                            String name = resultSet.getString("Name");
                            String phone = resultSet.getString("Phone");
                            return new User(userId, name, phone);
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
                return null;
            }

            @Override
            protected void onPostExecute(User userDetails) {
                if (userDetails != null) {
                    String nameFir = userDetails.getName();
                    String phonFir = userDetails.getPhone();
                    User user = new User(userDetails.getIdUser(), nameFir, phonFir);
                    setUsername(user);
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công " + nameFir + " " + phonFir + " " + userDetails.getIdUser(), Toast.LENGTH_SHORT).show();
//                    setUsername(nameFir, phonFir); // Gọi hàm để đặt thông tin người dùng
                    Intent intent = new Intent(LoginActivity.this, User_Main_Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Ktra lai thong tin", Toast.LENGTH_SHORT).show();
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

    void setUsername(User user) {
        FirebaseUntil.allUserCollectionReference().add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Tài liệu đã được thêm thành công
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý lỗi ở đây
                Log.e("Firebase", "Lỗi: " + e.getMessage());
            }
        });
        ;
    }

//    void getUsername(){
//        FirebaseUntil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    user =  task.getResult().toObject(User.class);
//                    if(user!=null){
//                        username.setText(user.getName());
//                    }
//                }
//            }
//        });
//    }



}