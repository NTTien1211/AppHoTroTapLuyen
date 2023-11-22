package com.example.app_hotrotapluyen.gym.User_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class User_Profile_Fragment extends Fragment {
    TextView User_Male_pt_inormation, User_phone_user_inormation, User_email_user_inormation,weight_ptIn_User,Height_ptIn_dgree
            ,BMI_user_profile ,User_user_inormation;
    Button btn_update_user_profile;
    UserModel userModel;
    ImageView profile_pic_image_view_profile;
    String idUser;
    LinearLayout color_back_bmi;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user__profile_, container, false);
        User_Male_pt_inormation = view.findViewById(R.id.User_Male_pt_inormation);
        User_phone_user_inormation = view.findViewById(R.id.User_phone_user_inormation);
        User_email_user_inormation = view.findViewById(R.id.User_email_user_inormation);
        User_user_inormation = view.findViewById(R.id.User_user_inormation);
        weight_ptIn_User = view.findViewById(R.id.weight_ptIn_User);
        Height_ptIn_dgree = view.findViewById(R.id.Height_ptIn_dgree);
        profile_pic_image_view_profile = view.findViewById(R.id.profile_pic_image_view_profile);
        BMI_user_profile = view.findViewById(R.id.BMI_user_profile);
        btn_update_user_profile = view.findViewById(R.id.btn_update_user_profile);
        color_back_bmi = view.findViewById(R.id.color_back_bmi);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute(idUser);

        btn_update_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(getActivity() , User_Repair_Inf_Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private class SelecDatabase extends AsyncTask<String, Void, UserModel> {

        @Override
        protected UserModel  doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {

                    String query = "SELECT * FROM Users WHERE ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(idUser));

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
                        String img = resultSet.getString("IMG");
                        userModel = new UserModel(Userid , name,phone,email,weight, hight ,gender,BMI, img);
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
            return  userModel ;
        }

        @Override
        protected void onPostExecute(UserModel userList) {
            super.onPostExecute(userList);
            if (userList != null ) {
                User_Male_pt_inormation.setText(userList.getGender());
                User_user_inormation.setText(userList.getName());
                User_phone_user_inormation.setText(userList.getPhone());
                User_email_user_inormation.setText(userList.getEmail());
                weight_ptIn_User.setText(userList.getWeight());
                Height_ptIn_dgree.setText(userList.getHight());
                String img = userList.getImg();
                if (img == null || img.isEmpty()){
                    profile_pic_image_view_profile.setImageDrawable(getResources().getDrawable(R.drawable.person_icon));
                }
                else {
                    Picasso.get().load(img).transform(new CircleTransform()).into(profile_pic_image_view_profile);
                }
                if (userList.getWeight() == null && userList.getHight() ==null ){
                    BMI_user_profile.setText("0");
                }
                else {

                    BMI_user_profile.setText(userList.getBMI());
                    String text =  userList.getBMI();
                    float bmi = Float.parseFloat(text.replace("," ,"."));
                    if (bmi <= 18.5) {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.lavender)); // Thay thế colorBlue bằng màu bạn muốn sử dụng
                    } else if (bmi <= 24.9) {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.bmi_overweight)); // Thay thế colorGreen bằng màu bạn muốn sử dụng
                    } else if (bmi <= 29.9) {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.bmi_obesity)); // Thay thế colorOrange bằng màu bạn muốn sử dụng
                    } else if (bmi <= 34.9) {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.bmi_underweight)); // Thay thế colorRed bằng màu bạn muốn sử dụng
                    } else {
                        color_back_bmi.setBackgroundColor(getResources().getColor(R.color.purple_dam)); // Thay thế colorPurple bằng màu bạn muốn sử dụng
                    }

                }

            } else {
                Toast.makeText(getContext(), "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }

}