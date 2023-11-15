package com.example.app_hotrotapluyen.gym.login_regis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView signupText;
    Button loginButton;
    UserModel userModel;
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


    public void loginAndFetchUserDetails(final String UserName, final String pass) {
        new AsyncTask<Void, Void, UserModel>() {
            @Override
            protected UserModel doInBackground(Void... params) {
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
                            return new UserModel(userId, name, phone);
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
            protected void onPostExecute(UserModel userDetails) {
                if (userDetails != null) {
                    String nameFir = userDetails.getName();
                    String phonFir = userDetails.getPhone();
                    UserModel user = new UserModel(userDetails.getIdUser(), nameFir, phonFir);

                    setUsername(user);
                    SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userID",userDetails.getIdUser());
                    editor.apply();
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

//    void setUsername(User user) {
//        FirebaseUntil.allUserCollectionReference().add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        // Tài liệu đã được thêm thành công
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // Xử lý lỗi ở đây
//                Log.e("Firebase", "Lỗi: " + e.getMessage());
//            }
//        });
//        ;
//    }
void setUsername(final UserModel user) {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {
        String userId = currentUser.getUid();
        String username = user.getName();
        if (userModel != null) {
            userModel.setName(username);
        } else {
            userModel = new UserModel(userId, user.getName(), user.getPhone());
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
        }
        // Kiểm tra xem người dùng đã tồn tại hay chưa bằng số điện thoại
}

//     void getUsername(){
//        FirebaseUntil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    userModel =    task.getResult().toObject(UserModel.class);
//                    if(userModel!=null){
////                        usernameInput.setText(userModel.getUsername());
//                    }
//                }
//            }
//        });
//    }



}