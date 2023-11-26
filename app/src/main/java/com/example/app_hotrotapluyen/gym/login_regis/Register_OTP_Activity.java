package com.example.app_hotrotapluyen.gym.login_regis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Register_OTP_Activity extends AppCompatActivity {

    EditText Regis_OTP_Editex;
    Button Regis_OTP_btnSend;
    Long timeoutSecon = 60L;
    ProgressBar progress_otp;
    TextView timeSecon_OTP;
    String phoneOTP , verificationCode , username , useremail, userphone , userpass;
    PhoneAuthProvider.ForceResendingToken ResendingToken;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otp);
        anhxa();
        phoneOTP = getIntent().getExtras().getString("phone");
        username = getIntent().getExtras().getString("name");
        useremail = getIntent().getExtras().getString("userEmail");
        userphone = getIntent().getExtras().getString("userPhone");
        userpass = getIntent().getExtras().getString("userPassword");
        senOTP(phoneOTP, false);

        Regis_OTP_btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkOTP = Regis_OTP_Editex.getText().toString();
                 PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,checkOTP);
                 singIn(credential);
                setInProgress(true);

            }
        });
        timeSecon_OTP.setOnClickListener((v)->{
            senOTP(phoneOTP,true);
        });


    }

    private void anhxa() {
        Regis_OTP_Editex = findViewById(R.id.Regis_OTP_Editex);
        Regis_OTP_btnSend = findViewById(R.id.Regis_OTP_btnSend);
        progress_otp = findViewById(R.id.progress_otp);
        timeSecon_OTP = findViewById(R.id.timeSecon_OTP);
    }
    void senOTP(String phoneNumber, boolean  isResend){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSecon, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    singIn(phoneAuthCredential);
                                    setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Register_OTP_Activity.this, "OTP Fail", Toast.LENGTH_SHORT).show();
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s ;
                                ResendingToken = forceResendingToken ;
                                Toast.makeText(Register_OTP_Activity.this, "OTP Successfully", Toast.LENGTH_SHORT).show();
                                setInProgress(false);
                                sendSms(userphone, "Your OTP is: " + verificationCode);
                            }
                        });
        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        }
        else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void singIn(PhoneAuthCredential phoneAuthCredential) {
            setInProgress(true);
            mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    setInProgress(false);
                    if (task.isSuccessful()){
                        Intent intent= new Intent(Register_OTP_Activity.this, LoginActivity.class);
                        new RegisterUserTask().execute(useremail, username, userpass, userphone);
                        intent.putExtra("phone" , phoneOTP);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Register_OTP_Activity.this, "OTP Fail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    void setInProgress (boolean inProgress){
        if (inProgress){
            progress_otp.setVisibility(View.VISIBLE);
            Regis_OTP_btnSend.setVisibility(View.GONE);
        }
        else {
            progress_otp.setVisibility(View.GONE);
            Regis_OTP_btnSend.setVisibility(View.VISIBLE);
        }
    }
    void startResendTimer() {
        timeSecon_OTP.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSecon--;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeSecon_OTP.setText("Resend OTP in " + timeoutSecon + " seconds");
                    }
                });
                if (timeoutSecon <= 0) {
                    timeoutSecon = 60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeSecon_OTP.setEnabled(true);
                        }
                    });
                }
            }
        }, 0, 1000);
    }
    private class RegisterUserTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String email = params[0];
                    String name = params[1];
                    String pass = params[2];
                    String phone = params[3];
                    int level = 0;


                    String query = "SELECT Email FROM Users WHERE Email = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, email);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        return false;
                    } else {
                        query = "INSERT INTO Users (Email, Name,Pass, Phone,Level ) VALUES (?, ?, ?, ? , ?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, email);
                        preparedStatement.setString(2, name);
                        preparedStatement.setString(3, pass);
                        preparedStatement.setString(4, phone);
                        preparedStatement.setString(5, String.valueOf(0));
//                        preparedStatement.setInt(5, level);

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
            progress_otp.setVisibility(View.GONE);
            if (success) {
                showSuccessMessage("Registration successful");
            } else {
                showErrorMessage("Registration failed");
            }
        }
    }

    private void showSuccessMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Register_OTP_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showErrorMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Register_OTP_Activity.this, message, Toast.LENGTH_SHORT).show();
                progress_otp.setVisibility(View.GONE);
            }
        });
    }
}