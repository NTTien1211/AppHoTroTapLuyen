package com.example.app_hotrotapluyen.gym.Admin;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.Admin.Adapter.Admin_Home_UserPT_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Grid_Home_UserPT_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.User_screen.User_Home_Fragment;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Admin_Home_Fragment extends Fragment {

    RecyclerView  recyclerView ;
    Admin_Home_UserPT_Adapter adminHomeUserPTAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin__home, container, false);
        recyclerView = view.findViewById(R.id.recycle_appli_pt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute();
        return  view;
    }
    private class SelecDatabase extends AsyncTask<String, Void, List<UserModel>> {
        List<UserModel> userList = new ArrayList<>();
        @Override
        protected List<UserModel>  doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {

                    String query = "SELECT * FROM Users WHERE Status = 'Waiting'";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);


                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String name = resultSet.getString("Name");
                        String Userid = resultSet.getString("ID_User");
                        String email = resultSet.getString("Email");
                        String weight = resultSet.getString("Weight");
                        String phone = resultSet.getString("Phone");
                        String hight = resultSet.getString("Height");
                        String gender = resultSet.getString("Gender");
                        String BMI = resultSet.getString("BMI");
                        String img = resultSet.getString("IMG");
                        String Level = resultSet.getString("Level");
                        String exper = resultSet.getString("Experience");
                        UserModel pt= new UserModel(Userid , name,phone,email,weight, hight ,gender,BMI,Integer.parseInt(Level) ,Integer.parseInt(exper), img);
                        userList.add(pt);
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
            return  userList ;
        }

        @Override
        protected void onPostExecute(List<UserModel> userList) {
            super.onPostExecute(userList);
            if (userList != null && userList.size() > 0) {
                // TODO: Use the result (UserModel) as needed
                adminHomeUserPTAdapter = new Admin_Home_UserPT_Adapter(userList , getActivity());
                recyclerView.setAdapter(adminHomeUserPTAdapter);
            } else {
                Toast.makeText(getActivity(), "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }

}