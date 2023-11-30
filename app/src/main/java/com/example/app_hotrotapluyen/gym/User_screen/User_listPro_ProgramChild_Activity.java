package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.ProGram_Day_User_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_Day_User_Child_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_UserPR_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.DayPRo_model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.Program_child_Model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.jdbcConnect.MediaManagerInitializer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_listPro_ProgramChild_Activity extends AppCompatActivity {
    Program_Day_User_Child_Adapter proGramDayUserAdapter;
    RecyclerView recycle_lispro_day_user;
    TextView tNameday,tSlbtapChild,tTimeChild;
    long idDay ;
    String level;
    String imageUrl;
    Button start_chill_exce;
    String dayname = "";
    FloatingActionButton add_program_pt_child;
    List<Program_child_Model> DAYMODEL;
     ImageView img_add_child_exper_update_pt_inormation;
    double sumcalo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_pro_program_child);
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idDay = sharedPreferences.getLong("id_idDay",0);
        level = sharedPreferences.getString("levelID","");
        MediaManagerInitializer.initializeMediaManager(this);
        recycle_lispro_day_user = findViewById(R.id.recycle_lispro_day_user_child);
        tNameday = findViewById(R.id.tNameDayChild);
        tSlbtapChild = findViewById(R.id.tSlbtapChild);
        tTimeChild = findViewById(R.id.tTimeChild);
        add_program_pt_child = findViewById(R.id.add_program_pt_child);
        start_chill_exce = findViewById(R.id.start_chill_exce);
        Toolbar actionBar = findViewById(R.id.toolbarChild);
        setToolbar(actionBar, "");
        DAYMODEL = new ArrayList<>();
        SelecDatabase selecDatabase1 = new SelecDatabase();
        selecDatabase1.execute();

        if (Integer.parseInt(level)  == 1 || Integer.parseInt(level)  == 2){
            add_program_pt_child.setVisibility(View.VISIBLE);
            add_program_pt_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hiển thị dialog thêm name và level
                    showAddDialog();
                }
            });

        }else {
            add_program_pt_child.setVisibility(View.GONE);
        }

        start_chill_exce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_listPro_ProgramChild_Activity.this, Pro_StartPlan_Activity.class);
                startActivity(intent);
            }
        });


    }
    private void showAddDialog() {
        // Sử dụng AlertDialog.Builder để tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add program");


        // Inflate layout cho dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.ptrainner_add_pro_child, null);
        builder.setView(dialogView);
//        dialogView.setBackgroundResource(R.drawable.rounded_background2);

        // Ánh xạ các thành phần trong dialog
        final EditText User_Name_exper_update_pt_inormation = dialogView.findViewById(R.id.User_Name_exper_update_pt_inormation);
        final EditText Information_add_child_exper_update_pt_inormation = dialogView.findViewById(R.id.Information_add_child_exper_update_pt_inormation);
        final EditText add_pro_chill_calo = dialogView.findViewById(R.id.add_pro_chill_calo);
        final EditText add_pro_chill_unil = dialogView.findViewById(R.id.add_pro_chill_unil);
        img_add_child_exper_update_pt_inormation = dialogView.findViewById(R.id.img_add_child_exper_update_pt_inormation);


        img_add_child_exper_update_pt_inormation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        // Xử lý sự kiện khi người dùng nhấn nút "OK"
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy giá trị từ EditText và Spinner
                Program_child_Model programModel = new Program_child_Model();
                programModel.setNameProChi(User_Name_exper_update_pt_inormation.getText().toString());
                programModel.setCalo(add_pro_chill_calo.getText().toString());
                programModel.setInfomat(Information_add_child_exper_update_pt_inormation.getText().toString());
                programModel.setImg(imageUrl);
                programModel.setUnil(Long.parseLong(add_pro_chill_unil.getText().toString()) );
                new AddDayToDatabase().execute(programModel);
                // Thực hiện các hành động với name và selectedLevel ở đây

                // Đóng dialog
                dialog.dismiss();
                recreate();
            }
        });

        // Xử lý sự kiện khi người dùng nhấn nút "Hủy"
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog
                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private class AddDayToDatabase extends AsyncTask<Program_child_Model, Void, Void> {

        @Override
        protected Void doInBackground(Program_child_Model... programChilds) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // Lấy thông tin từ tham số
                    Program_child_Model programChild = programChilds[0];

                    // Thực hiện insert
                    insertDay(connection, programChild);

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (java.sql.SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return null;
        }

        // Thực hiện insert ngày mới
        private void insertDay(Connection connection, Program_child_Model programChild) throws SQLException {
            String insertQuery = "INSERT INTO Program_Child (Name, Calo, Information, Img, Unit, ID_Day) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, programChild.getNameProChi());
                preparedStatement.setString(2, programChild.getCalo());
                preparedStatement.setString(3, programChild.getInfomat());
                preparedStatement.setString(4, programChild.getImg());
                preparedStatement.setString(5, String.valueOf( programChild.getUnil()));
                preparedStatement.setLong(6, idDay);
                preparedStatement.executeUpdate();
            } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
            }
        }
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
                Toast.makeText(User_listPro_ProgramChild_Activity.this, "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(User_listPro_ProgramChild_Activity.this, "Đang tải lên...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Xử lý sự thay đổi trong quá trình tải lên
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = (String) resultData.get("secure_url");
                        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(img_add_child_exper_update_pt_inormation);
                        Toast.makeText(User_listPro_ProgramChild_Activity.this, "Tải lên thành công!" + imageUrl, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(User_listPro_ProgramChild_Activity.this, "Tải lên thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Xử lý khi cần lên lịch lại việc tải lên
                    }
                })
                .option("folder", "android_upload")
                .dispatch();
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
    private class  SelecDatabase extends AsyncTask<String, Void, List<Program_child_Model>> {

        @Override
        protected List<Program_child_Model> doInBackground(String... strings) {
            DAYMODEL = new ArrayList<>();
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Day.Day AS DayName, Program_Child.Name AS ProgramChildName, Program_Child.Calo AS  Program_ChildCalo, Program_Child.Unit AS ProgramChildUnit, Program_Child.Img AS Program_ChildImg " +
                            "FROM Day " +
                            "JOIN Program_Child ON Day.ID_Day = Program_Child.ID_Day " +
                            "JOIN Program ON Day.ID_Pro = Program.ID_Pro " +
                            "WHERE Day.ID_Day = ?";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setLong(1, idDay);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String name = resultSet.getString("ProgramChildName");
                        String unit = resultSet.getString("ProgramChildUnit");
                        String img = resultSet.getString("Program_ChildImg");
                        dayname = resultSet.getString("DayName");
                        Program_child_Model pt = new Program_child_Model(name, Long.valueOf(unit), img);
                        DAYMODEL.add(pt);
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
            return DAYMODEL;
        }
        @Override
        protected void onPostExecute(List<Program_child_Model> program_child_Model) {
            super.onPostExecute(program_child_Model);
            if (program_child_Model != null && program_child_Model.size() > 0) {
                recycle_lispro_day_user.setLayoutManager(new LinearLayoutManager(User_listPro_ProgramChild_Activity.this,RecyclerView.VERTICAL,false));
                proGramDayUserAdapter = new Program_Day_User_Child_Adapter(program_child_Model , User_listPro_ProgramChild_Activity.this);
                recycle_lispro_day_user.setAdapter(proGramDayUserAdapter);
                tNameday.setText(dayname);
                tSlbtapChild.setText(String.valueOf(program_child_Model.size()));

            } else {
                Toast.makeText(User_listPro_ProgramChild_Activity.this, "No data ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}