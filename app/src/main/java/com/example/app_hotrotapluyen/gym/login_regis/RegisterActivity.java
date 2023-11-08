package com.example.app_hotrotapluyen.gym.login_regis;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, email, phone, password, rePassword;
    private Button registerButton;
    CountryCodePicker countryCodePicker;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_gmail);
        phone = findViewById(R.id.register_phone);
        password = findViewById(R.id.register_password);
        rePassword = findViewById(R.id.register_repair_password);
        registerButton = findViewById(R.id.RegisterButton);
        progressBar = findViewById(R.id.progess);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        countryCodePicker.registerCarrierNumberEditText(phone);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = username.getText().toString();
                String userEmail = email.getText().toString();
                String userPhone = phone.getText().toString();
                String userPassword = password.getText().toString();
                String confirmPassword = rePassword.getText().toString();
                userPhone = countryCodePicker.getFullNumberWithPlus();
                if (name.isEmpty() || userEmail.isEmpty() || userPhone.isEmpty() || userPassword.isEmpty()) {
                    showErrorMessage("Please fill in all fields");
                } else if (!userPassword.equals(confirmPassword)) {
                    showErrorMessage("Passwords do not match");
                } else if (userPassword.length() < 6) {
                    showErrorMessage("Password must be at least 6 characters");
                } else {
                    new RegisterUserTask().execute(name, userEmail, userPhone, userPassword);
                }
            }
        });

        Map<String, String> data= new HashMap<>();
        FirebaseFirestore.getInstance().collection("test").add(data);
    }

    private void showErrorMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private class RegisterUserTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String name = params[0];
                    String email = params[1];
                    String phone = params[2];
                    String pass = params[3];

                    String query = "SELECT Email FROM Users WHERE Email = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, email);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        return false;
                    } else {
                        query = "INSERT INTO Users (Name, Email, Phone, Pass) VALUES (?, ?, ?, ?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, phone);
                        preparedStatement.setString(4, pass);

                        int rowsInserted = preparedStatement.executeUpdate();
                        return rowsInserted > 0;
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
            progressBar.setVisibility(View.GONE);
            if (success) {
                showSuccessMessage("Registration successful");
                Intent intent = new Intent(RegisterActivity.this, Register_OTP_Activity.class);
                intent.putExtra("phone" , countryCodePicker.getFullNumberWithPlus());
                startActivity(intent);
            } else {
                showErrorMessage("Registration failed");
            }
        }
    }

    private void showSuccessMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}