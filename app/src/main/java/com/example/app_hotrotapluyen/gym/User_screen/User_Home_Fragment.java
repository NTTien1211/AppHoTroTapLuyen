package com.example.app_hotrotapluyen.gym.User_screen;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Grid_Home_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Grid_Home_UserPT_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.HomeU_pt;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class User_Home_Fragment extends Fragment {
    private RecyclerView recyclerView2;
    private Grid_Home_UserPT_Adapter userAdapter2;
    List<HomeU_pt> userList;
    private DrawerLayout drawerLayout;
    private Button btnOpenDrawer;
    public User_Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user__home_, container, false);
//        drawerLayout = view.findViewById(R.id.drawer_layout);
//        btnOpenDrawer = view.findViewById(R.id.left_cate_HomeU);
        List<Integer> items = new ArrayList<>();
        items.add(R.layout.item_grid_schedule);
        items.add(R.layout.item_grid_schedule);
        items.add(R.layout.item_grid_schedule);

        // Thiết lập RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView2 = view.findViewById(R.id.recyclerViewPTrainer);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


//        userList.add(new HomeU_pt("User 1", 5, 2, 4.5));
//        userList.add(new HomeU_pt("User 2", 3, 1, 4.2));
//        userList.add(new HomeU_pt("User 3", 7, 3, 4.8));



        Grid_Home_Adapter adapter = new Grid_Home_Adapter(items);
        recyclerView.setAdapter(adapter);
        userList = new ArrayList<>();

        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute();

//        btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (drawerLayout.isDrawerOpen(drawerLayout.getChildAt(1))) {
//                    drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
//                } else {
//                    drawerLayout.openDrawer(drawerLayout.getChildAt(1));
//                }
//            }
//        });
        return view;
    }

    private class SelecDatabase extends AsyncTask<String, Void, List<HomeU_pt> > {
        List<HomeU_pt> userList = new ArrayList<>();
        @Override
        protected List<HomeU_pt>  doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {

                    String query = "SELECT * FROM PTrainer ";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);


                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String name = resultSet.getString("Name");
                        Float rate = resultSet.getFloat("Rate");
                        DecimalFormat decimalFormat = new DecimalFormat("#,#");
                        float rateFormat = Float.parseFloat(decimalFormat.format(rate));
                        int people = resultSet.getInt("People");
                        int Experience = resultSet.getInt("Experience");
                        HomeU_pt pt = new HomeU_pt(name,Experience,people, rateFormat);
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
        protected void onPostExecute(List<HomeU_pt> userList) {
            super.onPostExecute(userList);
            if (userList != null && userList.size() > 0) {
                // TODO: Use the result (UserModel) as needed
                userAdapter2 = new Grid_Home_UserPT_Adapter(userList , getActivity());
                recyclerView2.setAdapter(userAdapter2);
            } else {
                Toast.makeText(getActivity(), "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }

}