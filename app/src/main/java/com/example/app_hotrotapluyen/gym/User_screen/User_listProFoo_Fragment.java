package com.example.app_hotrotapluyen.gym.User_screen;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Grid_Home_UserPT_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.ProGram_Day_User_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_UserPR_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.HomeU_pt;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramViewModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;

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
    Program_UserPR_Adapter programUserPRAdapter;
    private ProgramViewModel programViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list_pro_foo_, container, false);

        recyclerView = view.findViewById(R.id.list_profoo_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemList = new ArrayList<>();
        SelecDatabase selecDatabase1 = new SelecDatabase();
        selecDatabase1.execute();

        return view;
    }
    private class SelecDatabase extends AsyncTask<String, Void, List<ProgramModel> > {
        List<ProgramModel> programModel = new ArrayList<>();
        @Override
        protected List<ProgramModel>  doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {

                    String query = "SELECT Program.ID_Pro AS ProgramId, Program.Name AS ProgramName, PTrainer.Name AS TrainerName, Program.Level " +
                            "FROM Program " +
                            "INNER JOIN PTrainer ON Program.ID_PT = PTrainer.ID_PT";


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