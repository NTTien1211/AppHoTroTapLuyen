package com.example.app_hotrotapluyen.gym.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.Admin.Adapter.Admin_Over_Br_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.BookModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Admin_Over_Browser_Fragment extends Fragment {
    RecyclerView recycle_browser_admin;
    Admin_Over_Br_Adapter adminOverBrAdapter;
    TextView TvHI;
    String level;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin__over__browser, container, false);
        recycle_browser_admin = view.findViewById(R.id.recycle_browser_admin);
        TvHI = view.findViewById(R.id.TvHI);
        recycle_browser_admin.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        level = sharedPreferences.getString("levelID" ,"");
        // Create an instance of the SelecDatabase class and execute it

        if ( Integer.parseInt(level) ==2){
            SelecDatabase selecDatabase = new SelecDatabase();
            selecDatabase.execute();
        }else {
            TvHI.setText("HISTORY");
            SelecDatabaseHIS selecDatabase1 = new SelecDatabaseHIS();
            selecDatabase1.execute();
        }
        return view;
    }

    private class SelecDatabase extends AsyncTask<String, Void, List<BookModel>> {
        List<BookModel> userList = new ArrayList<>();

        @Override
        protected List<BookModel> doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Book.ID_Book, Book.ID_User_Give, Book.Time, Book.Money, Book.Status, Book.ID_User, Users1.Name AS UserName, Users1.IMG AS UserIMG, Users2.Name AS GiverName, Users2.IMG AS GiverIMG " +
                            "FROM Book " +
                            "INNER JOIN Users AS Users1 ON Book.ID_User = Users1.ID_User " +
                            "INNER JOIN Users AS Users2 ON Book.ID_User_Give = Users2.ID_User " +
                            "WHERE Book.Status = 'waiting'";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        Long id = resultSet.getLong("ID_Book");
                        Long userPTId = resultSet.getLong("ID_User_Give");
                        String giverName = resultSet.getString("GiverName");
                        String giverImg = resultSet.getString("GiverIMG");
                        UserModel userPT = new UserModel(String.valueOf(userPTId), giverName, giverImg);

                        Timestamp time = resultSet.getTimestamp("Time");
                        double money = resultSet.getDouble("Money");
                        String status = resultSet.getString("Status");

                        Long userUSId = resultSet.getLong("ID_User");
                        String userName = resultSet.getString("UserName");
                        String userImg = resultSet.getString("UserIMG");
                        UserModel userUS = new UserModel(String.valueOf(userUSId), userName, userImg);

                        BookModel bookModel = new BookModel(id, userPT, time, money, status, userUS);
                        userList.add(bookModel);
                }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "BookingTTTTTTT: " + e);
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e("TAG", "BookingTTTTTTT222222: " + e);
                    }
                }
            }
            return userList;
        }

        @Override
        protected void onPostExecute(List<BookModel> userList) {
            super.onPostExecute(userList);
            if (userList != null && userList.size() > 0) {
                // TODO: Use the result (UserModel) as needed
                adminOverBrAdapter = new Admin_Over_Br_Adapter(userList, getActivity());
                recycle_browser_admin.setAdapter(adminOverBrAdapter);
            } else {
                Toast.makeText(getActivity(), "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class SelecDatabaseHIS extends AsyncTask<String, Void, List<BookModel>> {
        List<BookModel> userList = new ArrayList<>();

        @Override
        protected List<BookModel> doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Book.ID_Book, Book.ID_User_Give, Book.Time, Book.Money, Book.Status, Book.ID_User, Users1.Name AS UserName, Users1.IMG AS UserIMG, Users2.Name AS GiverName, Users2.IMG AS GiverIMG " +
                            "FROM Book " +
                            "INNER JOIN Users AS Users1 ON Book.ID_User = Users1.ID_User " +
                            "INNER JOIN Users AS Users2 ON Book.ID_User_Give = Users2.ID_User " +
                            "WHERE Book.Status = 'accept'";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        Long id = resultSet.getLong("ID_Book");
                        Long userPTId = resultSet.getLong("ID_User_Give");
                        String giverName = resultSet.getString("GiverName");
                        String giverImg = resultSet.getString("GiverIMG");
                        UserModel userPT = new UserModel(String.valueOf(userPTId), giverName, giverImg);

                        Timestamp time = resultSet.getTimestamp("Time");
                        double money = resultSet.getDouble("Money");
                        String status = resultSet.getString("Status");

                        Long userUSId = resultSet.getLong("ID_User");
                        String userName = resultSet.getString("UserName");
                        String userImg = resultSet.getString("UserIMG");
                        UserModel userUS = new UserModel(String.valueOf(userUSId), userName, userImg);

                        BookModel bookModel = new BookModel(id, userPT, time, money, status, userUS);
                        userList.add(bookModel);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "BookingTTTTTTT: " + e);
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e("TAG", "BookingTTTTTTT222222: " + e);
                    }
                }
            }
            return userList;
        }

        @Override
        protected void onPostExecute(List<BookModel> userList) {
            super.onPostExecute(userList);
            if (userList != null && userList.size() > 0) {
                // TODO: Use the result (UserModel) as needed
                adminOverBrAdapter = new Admin_Over_Br_Adapter(userList, getActivity());
                recycle_browser_admin.setAdapter(adminOverBrAdapter);
            } else {
                Toast.makeText(getActivity(), "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
