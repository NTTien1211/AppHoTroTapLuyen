package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_Day_User_Child_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.FoodModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.Program_child_Model;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Pro_StartPlan_Activity extends AppCompatActivity {

    int myProgress = 0;
    ProgressBar progressBarView ,view_progress_barwait;
    FloatingActionButton btn_next , btn_prev;
    Button btn_nextCoudwait;
    TextView tv_time ,tv_timerwait;
    TextView et_timer ,tv_nextprogram;
    ImageView img_Nextprogram_starplan , img_program_starplan;
    int progress;
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimer1;
    int endTime = 250;
    long idDay ;
    String idUser;
    List<Program_child_Model> programChildModel;
    private int currentProgramIndex = 0;
    Handler handler;
    RelativeLayout layout_mainwait,layout_main;
    double sumcalo = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_start_plan);

        progressBarView = findViewById(R.id.view_progress_bar);
        btn_next = findViewById(R.id.btn_next_startplan);
        btn_prev = findViewById(R.id.btn_previ_startplan);
        tv_time= findViewById(R.id.tv_timer);
        et_timer = findViewById(R.id.et_timer);
        view_progress_barwait = findViewById(R.id.view_progress_barwait);
        img_Nextprogram_starplan = findViewById(R.id.img_Nextprogram_starplan);
        img_program_starplan = findViewById(R.id.img_program_starplan);
        tv_timerwait =findViewById(R.id.tv_timerwait);
        tv_nextprogram =findViewById(R.id.tv_nextprogram);
        btn_nextCoudwait =findViewById(R.id.btn_nextCoudwait);
        layout_mainwait = findViewById(R.id.layout_mainwait);
        layout_main = findViewById(R.id.layout_main);

        layout_main.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        idDay = sharedPreferences.getLong("id_idDay",0);


        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);

        RotateAnimation makeVertical1 = new RotateAnimation(0, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        makeVertical1.setFillAfter(true);
        view_progress_barwait.startAnimation(makeVertical);
        view_progress_barwait.setSecondaryProgress(endTime);
        view_progress_barwait.setProgress(0);
        programChildModel = new ArrayList<>();
        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute();


        // Bắt đầu đếm ngược

    }
    private void startCountdown() {

        fn_countdownWait();

    }
    private void fn_countdown( long unit) {
        layout_main.setVisibility(View.VISIBLE);
        String gifUrl = programChildModel.get(currentProgramIndex).getImg();;
        et_timer.setText(programChildModel.get(currentProgramIndex).getNameProChi());
        // Sử dụng Glide để tải và hiển thị GIF
        Glide.with(this)
                .asGif()
                .load(gifUrl)
                .into(img_program_starplan);
        if (unit >0) {
            myProgress = 0;

            try {
                countDownTimer.cancel();

            } catch (Exception e) {

            }
            progress = 1;
            endTime = (int) unit; // up to finish time

            countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime);
                    progress = progress + 1;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    String newtime =  minutes + ":" + seconds;

                    if (newtime.equals("0:0:0")) {
                        tv_time.setText("00:00:00");
                    } else if ( (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + minutes + ":0" + seconds);
                    } else if ( (String.valueOf(minutes).length() == 1)) {
                        tv_time.setText("0" + minutes + ":" + seconds);
                    } else if ((String.valueOf(seconds).length() == 1)) {
                        tv_time.setText(  minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText( "0" + minutes + ":0" + seconds);
                    }
                     else if (String.valueOf(minutes).length() == 1) {
                        tv_time.setText("0" + minutes + ":" + seconds);
                    } else if (String.valueOf(seconds).length() == 1) {
                        tv_time.setText( minutes + ":0" + seconds);
                    } else {
                        tv_time.setText( minutes + ":" + seconds);
                    }

                }

                @Override
                public void onFinish() {
                    setProgress(progress, endTime);
                    fn_countdownWait();

                }
            };
            countDownTimer.start();

        }else {
            Toast.makeText(getApplicationContext(),"Please enter the value",Toast.LENGTH_LONG).show();
        }


    }
    private void fn_countdownWait() {

        if (currentProgramIndex < programChildModel.size()) {
            layout_main.setVisibility(View.GONE);
            tv_nextprogram.setText(programChildModel.get(currentProgramIndex).getNameProChi());
            String gifUrl = programChildModel.get(currentProgramIndex).getImg();

            // Sử dụng Glide để tải và hiển thị GIF
            Glide.with(this)
                    .asGif()
                    .load(gifUrl)
                    .into(img_Nextprogram_starplan);
            // Retrieve the time interval from the programChildModel
            int timeInterval = 20 ;
            progress = 1;
            endTime = timeInterval; // up to finish time

            countDownTimer1 = new CountDownTimer(endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgressWait(progress, endTime);
                    progress = progress + 1;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    String newtime = minutes + ":" + seconds;

                    if (newtime.equals("0:0:0")) {
                        tv_timerwait.setText("00:00:00");
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_timerwait.setText("0" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1)) {
                        tv_timerwait.setText("0" + minutes + ":" + seconds);
                    } else if ((String.valueOf(seconds).length() == 1)) {
                        tv_timerwait.setText(minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_timerwait.setText("0" + minutes + ":0" + seconds);
                    } else if (String.valueOf(minutes).length() == 1) {
                        tv_timerwait.setText("0" + minutes + ":" + seconds);
                    } else if (String.valueOf(seconds).length() == 1) {
                        tv_timerwait.setText(minutes + ":0" + seconds);
                    } else {
                        tv_timerwait.setText(minutes + ":" + seconds);
                    }
                }

                @Override
                public void onFinish() {
                    setProgressWait(progress, endTime);
                    fn_countdown(programChildModel.get(currentProgramIndex).getUnil());
                    currentProgramIndex++;

                }
            };
            countDownTimer1.start();
        }else {
            showCongratulatoryDialog("Congratulations! You've completed the program.");
            new BookPTTask().execute(String.valueOf(sumcalo));

        }
//        btn_nextCoudwait.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fn_countdown(programChildModel.get(currentProgramIndex).getUnil());
//            }
//        });

    }
    private class BookPTTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // Lấy thông tin người được book (ID_User_Give)
                    String CaloOut = strings[0];

                    // Lấy thông tin người dùng (ID_User)

                    // Lấy thời gian hiện tại
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    String currentDate = year + "-" + month + "-" + day;

                    String selectQuery = "SELECT * FROM Calodi WHERE Time = ? AND ID_User = ?";
                    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                    selectStatement.setDate(1, Date.valueOf(currentDate));
                    selectStatement.setLong(2, Long.parseLong(idUser));
                    ResultSet resultSet = selectStatement.executeQuery();
                    // Thêm dữ liệu vào bảng Book
                    if (resultSet.next()) {
                        String query = "UPDATE Calodi Set CaloOUT =? WHERE Time = ? AND ID_User = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, CaloOut);
                        preparedStatement.setDate(2, Date.valueOf(currentDate));
                        preparedStatement.setString(3, idUser);
                        int rowsAffected = preparedStatement.executeUpdate();
                        return rowsAffected > 0;
                    }else {
                        String query = "INSERT INTO Calodi (Time,CaloOUT, ID_User) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setDate(1, Date.valueOf(currentDate));
                        preparedStatement.setString(2, CaloOut);
                        preparedStatement.setString(3, idUser);
                        int rowsAffected = preparedStatement.executeUpdate();
                        return rowsAffected > 0;
                    }



                    // Đặt Status là "waiting" (hoặc giá trị khác tùy theo yêu cầu)

                    // Thực hiện truy vấn và cập nhật bảng Book


                    // Trả về true nếu có ít nhất một hàng bị ảnh hưởng (truy vấn thành công)


                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "prinrrrr: " + e );
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Trả về false nếu có lỗi xảy ra hoặc không có hàng nào bị ảnh hưởng
            return false;
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(Pro_StartPlan_Activity.this, "Success", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(Pro_StartPlan_Activity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showCongratulatoryDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the click on the OK button if needed
                        onBackPressed();
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);

    }

    public void setProgressWait(int startTime, int endTime) {
        view_progress_barwait.setMax(endTime);
        view_progress_barwait.setSecondaryProgress(endTime);
        view_progress_barwait.setProgress(startTime);

    }
    private class  SelecDatabase extends AsyncTask<String, Void, List<Program_child_Model>> {
        String unil;
        @Override
        protected List<Program_child_Model> doInBackground(String... strings) {
            programChildModel = new ArrayList<>();
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Day.Day AS DayName, Program_Child.Name AS ProgramChildName, Program_Child.Calo as Program_ChildCalo, Program_Child.Unit as ProgramChildUnit, Program_Child.Img as  Program_ChildImg " +
                            "FROM Day " +
                            "JOIN Program_Child ON Day.ID_Day = Program_Child.ID_Day " +
                            "JOIN Program ON Day.ID_Pro = Program.ID_Pro " +
                            "WHERE Day.ID_Day = ?";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setLong(1, idDay);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String name = resultSet.getString("ProgramChildName");
                        String calo = resultSet.getString("Program_ChildCalo");
                        String img = resultSet.getString("Program_ChildImg");
                        unil = resultSet.getString("ProgramChildUnit");
                        sumcalo += Double.parseDouble(calo);
                        Program_child_Model pt = new Program_child_Model(name,  Long.valueOf(unil) , img);
                        programChildModel.add(pt);
                    }


                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return programChildModel;
        }
        @Override
        protected void onPostExecute(List<Program_child_Model> program_child_Model) {
            super.onPostExecute(program_child_Model);
            if (program_child_Model != null && program_child_Model.size() > 0) {

//                recycle_lispro_day_user.setLayoutManager(new LinearLayoutManager(Pro_StartPlan_Activity.this, RecyclerView.VERTICAL,false));
                    startCountdown();

            } else {
                Toast.makeText(Pro_StartPlan_Activity.this, "AAAAAA ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}