package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.PTrainer.Upload_gif_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
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
import java.util.HashMap;
import java.util.Map;

public class User_Repair_Inf_Activity extends AppCompatActivity {
    private SeekBar decimalSeekBar;
    private TextView valueTextView;
    EditText User_Name_repair_pt_inormation , User_Phone_repair_pt_inormation, User_Email_repair_pt_inormation;
    String idUser;
    String selecvalue;
    int selectedValue;
    String  seekbl;
    String imageUrl;
    NumberPicker verticalNumberPicker;
    UserModel userModel;
    Button btn_update_userSave_profile;
    RadioGroup genderRadioGroup;
    ImageView profile_pic_image_view_repairt_profile;
    private static boolean isMediaManagerInitialized = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_repair_inf);
        MediaManagerInitializer.initializeMediaManager(this);





        SharedPreferences sharedPreferences = getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        Toolbar actionBar = findViewById(R.id.toolbar_userrepair);
        setToolbar(actionBar, "");
        anhxa();


        verticalNumberPicker.setMinValue(130);
        verticalNumberPicker.setMaxValue(250);
        verticalNumberPicker.setWrapSelectorWheel(true); // Cho phép lặp lại giá trị khi cuộn



        String[] displayedValues = new String[121];
        for (int i = 0; i <= 120; i++) {
            displayedValues[i] = String.valueOf(130 + i);
        }
        verticalNumberPicker.setDisplayedValues(displayedValues);
        verticalNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedValue = newVal;
            }
        });


        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selecradio = findViewById(i);
                if (selecradio!= null){
                     selecvalue  = selecradio.getText().toString();
                }
            }
        });

        decimalSeekBar.setMax(3000); // Số bước (steps) có thể tùy chỉnh tùy theo yêu cầu của bạn

        decimalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Chia nhỏ giá trị SeekBar thành số thập phân
                double decimalValue = progress / 10.0;

                // Hiển thị giá trị số thập phân trong TextView hoặc thực hiện xử lý tương ứng
                valueTextView.setText(String.valueOf(decimalValue));
                seekbl = valueTextView.getText().toString();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Xử lý khi bắt đầu chạm vào SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Xử lý khi kết thúc chạm vào SeekBar
            }
        });
        profile_pic_image_view_repairt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        btn_update_userSave_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RegisterUserTask().execute(User_Phone_repair_pt_inormation.getText().toString(), User_Email_repair_pt_inormation.getText().toString()
                        ,User_Name_repair_pt_inormation.getText().toString(),selecvalue , seekbl ,String.valueOf(selectedValue) , imageUrl);

                onBackPressed();

            }
        });
    }

    private void anhxa() {
        decimalSeekBar = findViewById(R.id.decimalSeekBar);
        valueTextView = findViewById(R.id.valueTextView);
        User_Name_repair_pt_inormation = findViewById(R.id.User_Name_repair_pt_inormation);
        User_Phone_repair_pt_inormation = findViewById(R.id.User_Phone_repair_pt_inormation);
        User_Email_repair_pt_inormation = findViewById(R.id.User_Email_repair_pt_inormation);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        verticalNumberPicker= findViewById(R.id.verticalNumberPicker);
        btn_update_userSave_profile= findViewById(R.id.btn_update_userSave_profile);
        profile_pic_image_view_repairt_profile= findViewById(R.id.profile_pic_image_view_repairt_profile);

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

    private class RegisterUserTask extends AsyncTask<String, Void, UserModel> {
        @Override
        protected UserModel doInBackground(String... params) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String phone = params[0];
                    String email = params[1];
                    String name = params[2];
                    String gender = params[3];
                    String weight = params[4];
                    String height = params[5];
                    String img = params[6];
                    double weightValue;
                    double heightValue;
                    double bmi;
                    if (weight == null){
                         weightValue = 0;
                    }else {
                         weightValue = Double.parseDouble(weight);
                    }
                    if (height == null) {
                        heightValue = 0;
                    } else {
                        heightValue = Double.parseDouble(height)/100.0;
                    }
                    bmi = weightValue / (heightValue * heightValue);
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    decimalFormat.setRoundingMode(RoundingMode.CEILING);

                    String formattedBmi = decimalFormat.format(bmi);

                    String query = "SELECT * FROM Users WHERE ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idUser);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        query = "UPDATE Users SET Phone=?, Email=?, Name=?, Gender=?, Weight=?, Height=?, BMI=? , IMG =? WHERE ID_User=?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, phone);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, name);
                        preparedStatement.setString(4, gender);
                        preparedStatement.setString(5, weight);
                        preparedStatement.setString(6, height);
                        preparedStatement.setString(7, formattedBmi);
                        preparedStatement.setString(8, img);
                        preparedStatement.setString(9, idUser); // ID_User để xác định dòng cần cập nhật

                        int rowsUpdated = preparedStatement.executeUpdate();
                        return userModel; //

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
                Toast.makeText(User_Repair_Inf_Activity.this, "Save profile success", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(User_Repair_Inf_Activity.this, "Save profile success", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private class GetUserTask extends AsyncTask<Void, Void, UserModel> {
        @Override
        protected UserModel doInBackground(Void... voids) {
            Connection connection = JdbcConnect.connect();
            UserModel userModel = null;

            if (connection != null) {
                try {
                    String query = "SELECT * FROM Users WHERE ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idUser);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String nameOut = resultSet.getString("Name");
                        String UseridOUT = resultSet.getString("ID_User");
                        String emailOUT = resultSet.getString("Email");
                        String weightOUT = resultSet.getString("Weight");
                        String phoneOUT = resultSet.getString("Phone");
                        String hightOUT = resultSet.getString("Height");
                        String genderOUT = resultSet.getString("Gender");
                        userModel = new UserModel(UseridOUT, nameOut, phoneOUT, emailOUT, weightOUT, hightOUT, genderOUT);
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
                updateUIWithUserData(success);
            } else {
                // Handle case when user data is not found
            }
        }
    }

    // Helper method to update UI with user data
    private void updateUIWithUserData(UserModel userData) {
        if (userData.getHight() == null) {
            verticalNumberPicker.setValue(150);
        } else {
            verticalNumberPicker.setValue(Integer.parseInt(userData.getHight()));
        }

        decimalSeekBar = findViewById(R.id.decimalSeekBar);
        if (userData.getWeight() == null) {
            valueTextView.setText("0.0");
        } else {
            valueTextView.setText(userData.getWeight());
        }

        User_Name_repair_pt_inormation.setText(userData.getName());
        User_Phone_repair_pt_inormation.setText(userData.getPhone());
        User_Email_repair_pt_inormation.setText(userData.getEmail());
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Tải ảnh lên Clouddinary
            try {
                uploadImageToCloudinary(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(User_Repair_Inf_Activity.this, "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToCloudinary(Uri imageUri) throws IOException {
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
                        Toast.makeText(User_Repair_Inf_Activity.this, "Đang tải lên...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Xử lý sự thay đổi trong quá trình tải lên
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = (String) resultData.get("secure_url");
                        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(profile_pic_image_view_repairt_profile);
                        Toast.makeText(User_Repair_Inf_Activity.this, "Tải lên thành công!" + imageUrl, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(User_Repair_Inf_Activity.this, "Tải lên thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
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