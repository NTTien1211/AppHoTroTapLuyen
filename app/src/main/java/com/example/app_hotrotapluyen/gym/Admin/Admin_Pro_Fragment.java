package com.example.app_hotrotapluyen.gym.Admin;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Admin_Pro_Fragment extends Fragment {
    TextView tvR, tvPython, tvCPP ,tvAdmin;
    PieChart pieChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin__pro, container, false);
        tvR = view.findViewById(R.id.tvR);
        tvPython = view.findViewById(R.id.tvPython);
        tvCPP = view.findViewById(R.id.tvCPP);
        tvAdmin = view.findViewById(R.id.tvAdmin);
        pieChart = view.findViewById(R.id.piechart);
        new  SelectDatabase().execute();

        return view;
    }
    private void setData() {
        // Set the percentage of language used

//
        // Create entries for the pie chart
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.parseFloat(tvR.getText().toString().replace("%", "")), "USER"));
        entries.add(new PieEntry(Float.parseFloat(tvPython.getText().toString().replace("%", "")), "PT"));
        entries.add(new PieEntry(Float.parseFloat(tvAdmin.getText().toString().replace("%", "")), "Admin"));


        // Set the data and color to the pie chart
        PieDataSet dataSet = new PieDataSet(entries, "User statistics");
        dataSet.setColors(
                Color.parseColor("#FFA726"),
                Color.parseColor("#66BB6A"),
                Color.parseColor("#EF5350"));


        PieData data = new PieData(dataSet);

        // Set any additional configuration for the PieChart if needed
        // pieChart.setHoleRadius(20f);

        pieChart.setData(data);
        pieChart.invalidate(); // Refresh the chart

        // To animate the pie chart
        pieChart.animateY(1000, Easing.EaseInOutCubic);
    }
    private class SelectDatabase extends AsyncTask<Void, Void, Map<String, Integer>> {
        @Override
        protected Map<String, Integer> doInBackground(Void... voids) {
            Map<String, Integer> result = new HashMap<>();
            Connection connection = JdbcConnect.connect();

            if (connection != null) {
                try {
                    // Đếm số User (level = 0)
                    String queryUserCount = "SELECT COUNT(*) AS UserCount FROM Users WHERE Level = 0";
                    PreparedStatement preparedStatementUserCount = connection.prepareStatement(queryUserCount);
                    ResultSet resultSetUserCount = preparedStatementUserCount.executeQuery();
                    if (resultSetUserCount.next()) {
                        int userCount = resultSetUserCount.getInt("UserCount");
                        result.put("UserCount", userCount);
                    }

                    // Đếm số Pt (level = 1)
                    String queryPtCount = "SELECT COUNT(*) AS PtCount FROM Users WHERE Level = 1";
                    PreparedStatement preparedStatementPtCount = connection.prepareStatement(queryPtCount);
                    ResultSet resultSetPtCount = preparedStatementPtCount.executeQuery();
                    if (resultSetPtCount.next()) {
                        int ptCount = resultSetPtCount.getInt("PtCount");
                        result.put("PtCount", ptCount);
                    }

                    // Tính tổng số người dùng (People)
                    String queryTotalPeople = "SELECT COUNT(*) AS TotalPeople FROM Users";
                    PreparedStatement preparedStatementTotalPeople = connection.prepareStatement(queryTotalPeople);
                    ResultSet resultSetTotalPeople = preparedStatementTotalPeople.executeQuery();
                    if (resultSetTotalPeople.next()) {
                        int totalPeople = resultSetTotalPeople.getInt("TotalPeople");
                        result.put("TotalPeople", totalPeople);
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

            return result;
        }

        @Override
        protected void onPostExecute(Map<String, Integer> result) {
            // Ở đây bạn có thể sử dụng kết quả trong UI hoặc thực hiện các xử lý khác theo nhu cầu của bạn.
            Integer userCountObject = result.get("UserCount");
            Integer ptCountObject = result.get("PtCount");
            Integer totalPeopleObject = result.get("TotalPeople");

            float userCount = (userCountObject != null && totalPeopleObject != null) ? (userCountObject.floatValue() / totalPeopleObject.floatValue() * 100) : 0;
            float ptCount = (ptCountObject != null && totalPeopleObject != null) ? (ptCountObject.floatValue() / totalPeopleObject.floatValue() * 100) : 0;
            float admin =  100 - userCount -ptCount;
            int totalPeople = (totalPeopleObject != null) ? totalPeopleObject.intValue() : 0;

            tvR.setText(String.format("%.0f%%", userCount));
            tvPython.setText(String.format("%.0f%%", ptCount));
            tvAdmin.setText(String.format("%.0f%%", admin));
            tvAdmin.setText(String.format("%.0f%%", ptCount));

            tvCPP.setText(Integer.toString(totalPeople));
            setData();


        }
    }


}