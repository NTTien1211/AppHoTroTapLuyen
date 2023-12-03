package com.example.app_hotrotapluyen.gym.User_screen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.PT_Over_PT_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Rate_Feedback_PT_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.BookModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.CalodiModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.FeedbackModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.jdbcConnect.MediaManagerInitializer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class User_Over_Fragment extends Fragment {
    BarChart barChartUserRent;
    ProgressBar progressBar;
    CalendarView calendarView;
    String level, idUser;
    CalodiModel calodiModel;
    RecyclerView recycle_over_pt;
    PT_Over_PT_Adapter ptOverPtAdapter;
    TextView mongmuon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_user__over, container, false);
         barChartUserRent = view.findViewById(R.id.chartListUserRent);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        level = sharedPreferences.getString("levelID" ,"");
        idUser = sharedPreferences.getString("userID" ,"");
        progressBar = view.findViewById(R.id.progressBarL);
        calendarView = view.findViewById(R.id.calendarView);
        mongmuon = view.findViewById(R.id.mongmuon);
        recycle_over_pt = view.findViewById(R.id.recycle_over_pt);
        recycle_over_pt.setLayoutManager(new LinearLayoutManager(getContext()));
        if (Integer.parseInt(level) == 1){
            TextView titi = view.findViewById(R.id.titi);
            titi.setVisibility(View.GONE);
            recycle_over_pt.setVisibility(View.VISIBLE);
            barChartUserRent.setVisibility(View.GONE);
            SelecDUserInday selecDUserInday = new SelecDUserInday();
            selecDUserInday.execute();
        }
        else {
            recycle_over_pt.setVisibility(View.GONE);
            barChartUserRent.setVisibility(View.VISIBLE);
            new CheckDesireAvailabilityTask().execute();
            showBarChartForCurrentDate();
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    // Get the selected date and execute AsyncTask
                    String formattedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    new SelectCalodi().execute(formattedDate);
                }
            });
        }




        return  view;
    }
    private void showDesireDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn mục tiêu của bạn");
        builder.setPositiveButton("Gain weight", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Gọi hàm updateDesireStatus với tham số "Tăng cân"
                new UpdateDesireStatusTask().execute("Gain weight");
            }
        });
        builder.setNegativeButton("Losing weight", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Gọi hàm updateDesireStatus với tham số "Giảm cân"
                new UpdateDesireStatusTask().execute("Losing weight");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private class UpdateDesireStatusTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Connection connection = JdbcConnect.connect();
            String desire = params[0];
            // Thực hiện cập nhật trạng thái desire cho toàn bộ row có ID_User = idUser
            if (connection != null) {
                try {
                    String updateQuery = "UPDATE Calodi SET Desire = ? WHERE ID_User = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, desire);
                    updateStatement.setLong(2, Long.parseLong(idUser));
                    updateStatement.executeUpdate();
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
        protected void onPostExecute(Void aVoid) {
            // Thực hiện các công việc cần thiết sau khi cập nhật xong, nếu có
            // Ví dụ: Hiển thị thông báo, làm mới giao diện, v.v.
        }
    }

    private class SelectCalodi extends AsyncTask<String, Void, List<CalodiModel>> {

        @Override
        protected List<CalodiModel> doInBackground(String... dates) {
            Connection connection = JdbcConnect.connect();
            List<CalodiModel> calodiModels = new ArrayList<>();

            if (connection != null) {
                try {
                    String query = "SELECT * FROM Calodi WHERE ID_User = ? AND Time = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setLong(1, Long.parseLong(idUser));
                    preparedStatement.setString(2, dates[0]); // Sử dụng chuỗi trực tiếp
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        Long id = resultSet.getLong("ID_Ca");
                        String caloOut = resultSet.getString("CaloOUT");
                        String caloin = resultSet.getString("calo_in");

                        CalodiModel calodiModel = new CalodiModel();
                        calodiModel.setID_Ca(id);
                        calodiModel.setCaloOut(caloOut);
                        calodiModel.setCaloIn(caloin);

                        calodiModels.add(calodiModel);
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
            return calodiModels;
        }


        @Override
        protected void onPostExecute(List<CalodiModel> calodiModels) {
            if (calodiModels != null && !calodiModels.isEmpty()) {
                // Handle the fetched data, update your UI or chart

                updateBarChart(calodiModels);
            } else {
                Toast.makeText(getContext(), "No data activity", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class CheckDesireAvailabilityTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Connection connection = JdbcConnect.connect();
            String desire ="" ;

            if (connection != null) {
                try {
                    String query = "SELECT  Desire FROM Calodi WHERE ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setLong(1, Long.parseLong(idUser));
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                            // desire đã có dữ liệu
                        desire = resultSet.getString("Desire");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("TAG", "doInBackgroundDesire: " +e);
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return desire;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.isEmpty()) {
                showDesireDialog();
                mongmuon.setText(result);
            } else {
                showBarChartForCurrentDate();
                mongmuon.setText(result);
            }
        }
    }

    private void showBarChartForCurrentDate() {
        Calendar currentCalendar = Calendar.getInstance();
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = currentYear + "-" + currentMonth + "-" + currentDay;
        new SelectCalodi().execute(currentDate);
    }

    private void updateBarChart(List<CalodiModel> calodiModels) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < calodiModels.size(); i++) {
            entries.add(new BarEntry(i, Float.parseFloat(calodiModels.get(i).getCaloIn())));
            entries.add(new BarEntry(i + 1, Float.parseFloat(calodiModels.get(i).getCaloOut())));
            entries.add(new BarEntry(i + 2, Float.parseFloat(calodiModels.get(i).getCaloIn()) +
                    Float.parseFloat(calodiModels.get(i).getCaloOut())));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Calo");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new DefaultValueFormatter(0));
        YAxis yAxis = barChartUserRent.getAxisLeft();
        yAxis.setAxisMinimum(1f);
        yAxis.setAxisMaximum(1000f);

        barChartUserRent.setFitBars(true);
        barChartUserRent.setData(barData);
        barChartUserRent.getDescription().setText("Day");
        barChartUserRent.animateY(2000);
        barChartUserRent.invalidate();
    }
    private class SelecDUserInday extends AsyncTask<String, Void, List<BookModel>> {
        List<BookModel> bookModels = new ArrayList<>();
        @Override
        protected List<BookModel> doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
//                    String idUserGive = strings[0]; // Assuming the ID_User_Give value is passed as the first parameter

                    String query = "SELECT Book.ID_Book,Book.timein_day as TimeIN, Book.ID_User_Give, Book.Time, Book.Money, Book.Status, Book.ID_User, Users1.Name AS UserName, Users1.IMG AS UserIMG, Users2.Name AS GiverName, Users2.IMG AS GiverIMG " +
                            "FROM Book " +
                            "INNER JOIN Users AS Users1 ON Book.ID_User = Users1.ID_User " +
                            "INNER JOIN Users AS Users2 ON Book.ID_User_Give = Users2.ID_User " +
                            "WHERE Book.ID_User_Give = ? AND Book.Status = 'accept' " +
                            "ORDER BY " +
                            "CASE " +
                            "   WHEN Book.timein_day = '5 AM - 7 AM' THEN 1 " +
                            "   WHEN Book.timein_day = '7 AM - 9 AM' THEN 2 " +
                            "   WHEN Book.timein_day = '2 PM - 4 PM' THEN 3 " +
                            "   WHEN Book.timein_day = '4 PM - 6 PM' THEN 4 " +
                            "   WHEN Book.timein_day = '6 PM - 8 PM' THEN 5 " +
                            "   ELSE 6 " +
                            "END";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idUser); // Set the parameter value

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
                        String timeinday = resultSet.getString("TimeIN");

                        Long userUSId = resultSet.getLong("ID_User");
                        String userName = resultSet.getString("UserName");
                        String userImg = resultSet.getString("UserIMG");
                        UserModel userUS = new UserModel(String.valueOf(userUSId), userName, userImg);

                        BookModel bookModel = new BookModel(id, userPT, time, money, status, userUS,timeinday);
                        bookModels.add(bookModel);
                    }

                    return bookModels;

                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "doInBackgroundCAT: "+ e );
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e("TAG", "BookingTTTTTTT222222: " + e);
                    }
                }
            }
            return null; // Handle the case when connection is null
        }

        @Override
        protected void onPostExecute(List<BookModel> bookModels) {
            if (bookModels != null && bookModels.size() > 0) {
                // Update data in the adapter and notify the RecyclerView of the changes.
                ptOverPtAdapter = new PT_Over_PT_Adapter(bookModels, getActivity());
                recycle_over_pt.setAdapter(ptOverPtAdapter);
//                Log.d("TAG", "onPostExecute: " + String.valueOf(bookModels.size()));
            } else {
//                Log.d("TAG", "onPostExecute: " +String.valueOf(bookModels.size()));
                // Handle when there is no data or an error occurs.
                Toast.makeText(getContext(), "No User data available", Toast.LENGTH_SHORT).show();
            }
        }
    }



}