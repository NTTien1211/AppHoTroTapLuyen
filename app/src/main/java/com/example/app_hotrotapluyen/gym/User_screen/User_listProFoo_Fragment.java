package com.example.app_hotrotapluyen.gym.User_screen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Grid_Home_UserPT_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.ProGram_Day_User_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_UserPR_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.HomeU_pt;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramViewModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class User_listProFoo_Fragment extends Fragment {
    RecyclerView recyclerView ;
    private List<ProgramModel> itemList;
    String level , idUser;
    Program_UserPR_Adapter programUserPRAdapter;
    FloatingActionButton add_program_pt;
    private ProgramViewModel programViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list_pro_foo_, container, false);

        recyclerView = view.findViewById(R.id.list_profoo_recycle);
        add_program_pt = view.findViewById(R.id.add_program_pt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        level = sharedPreferences.getString("levelID" ,"");
        idUser = sharedPreferences.getString("userID" ,"");
        itemList = new ArrayList<>();
        SelecDatabase selecDatabase1 = new SelecDatabase();
        selecDatabase1.execute();

        if (Integer.parseInt(level)  == 1){
            add_program_pt.setVisibility(View.VISIBLE);
            add_program_pt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hiển thị dialog thêm name và level
                    showAddDialog(getContext());
                }
            });

        }else {
            add_program_pt.setVisibility(View.GONE);
        }

        return view;
    }

    private void showAddDialog(Context context) {
        // Sử dụng AlertDialog.Builder để tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add program");


        // Inflate layout cho dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.activity_ptrainer_add_program, null);
        builder.setView(dialogView);
//        dialogView.setBackgroundResource(R.drawable.rounded_background2);

        // Ánh xạ các thành phần trong dialog
        final EditText nameEditText = dialogView.findViewById(R.id.ptPro_Name_pt_inormation);
        final Spinner levelSpinner = dialogView.findViewById(R.id.levelSpinner);

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.levels_array, android.R.layout.simple_spinner_item);

        // Đặt layout cho dropdown của Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Đặt ArrayAdapter vào Spinner
        levelSpinner.setAdapter(adapter);

        // Xử lý sự kiện khi người dùng nhấn nút "OK"
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy giá trị từ EditText và Spinner
                String name = nameEditText.getText().toString();
                String selectedLevel = levelSpinner.getSelectedItem().toString();
                ProgramModel programModel = new ProgramModel();
                programModel.setNamePro(name);
                programModel.setLevel(selectedLevel);
                new  AddProgramToDatabase().execute(programModel);
                // Thực hiện các hành động với name và selectedLevel ở đây

                // Đóng dialog
                dialog.dismiss();
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
    private class AddProgramToDatabase extends AsyncTask<ProgramModel, Void, Void> {

        @Override
        protected Void doInBackground(ProgramModel... programs) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // Truy vấn SQL để thêm chương trình mới
                    String insertQuery = "INSERT INTO Program (Name, ID_User, Level) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                    // Lấy chương trình từ tham số
                    ProgramModel program = programs[0];

                    // Đặt giá trị cho các tham số trong truy vấn
                    if (programExists(connection, Long.parseLong(idUser), program.getLevel())) {
                        // Nếu tồn tại, thực hiện update
                        updateProgram(connection, Long.parseLong(idUser), program);
                    } else {
                        // Nếu chưa tồn tại, thực hiện insert
                        insertProgram(connection, Long.parseLong(idUser), program);
                    }

                    // Thực hiện truy vấn INSERT
                    preparedStatement.executeUpdate();

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
    }
    private boolean programExists(Connection connection, long userId, String level) throws SQLException {
        String selectQuery = "SELECT COUNT(*) FROM Program WHERE ID_User = ? AND Level = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, level);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    // Thực hiện update chương trình
    private void updateProgram(Connection connection, long userId, ProgramModel program) throws SQLException {
        String updateQuery = "UPDATE Program SET Name = ? WHERE ID_User = ? AND Level = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, program.getNamePro());
            preparedStatement.setLong(2, userId);
            preparedStatement.setString(3, program.getLevel());
            preparedStatement.executeUpdate();
        }
    }

    // Thực hiện insert chương trình mới
    private void insertProgram(Connection connection, long userId, ProgramModel program) throws SQLException {
        String insertQuery = "INSERT INTO Program (Name, ID_User, Level) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, program.getNamePro());
            preparedStatement.setLong(2, userId);
            preparedStatement.setString(3, program.getLevel());
            preparedStatement.executeUpdate();
        }
    }
    private class SelecDatabase extends AsyncTask<String, Void, List<ProgramModel> > {
        List<ProgramModel> programModel = new ArrayList<>();
        @Override
        protected List<ProgramModel>  doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {

                    String query = "SELECT Program.ID_Pro AS ProgramId, Program.Name AS ProgramName, Users.Name AS TrainerName, Program.Level " +
                            "FROM Program " +
                            "INNER JOIN Users ON Program.ID_User = Users.ID_User";


                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        int idPro = resultSet.getInt("ProgramId");
                        String name = resultSet.getString("ProgramName");
                        String PTname = resultSet.getString("TrainerName");
                        String level = resultSet.getString("Level");
                        ProgramModel pt = new ProgramModel(idPro,name, PTname, level);
                        programModel.add(pt);
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
            return  programModel ;
        }

        @Override
        protected void onPostExecute(List<ProgramModel> programModel) {
            super.onPostExecute(programModel);
            if (programModel != null && programModel.size() > 0) {
                // TODO: Use the result (UserModel) as needed
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
                programUserPRAdapter = new Program_UserPR_Adapter(programModel);
                recyclerView.setAdapter(programUserPRAdapter);

            } else {
                Toast.makeText(getActivity(), "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }




}