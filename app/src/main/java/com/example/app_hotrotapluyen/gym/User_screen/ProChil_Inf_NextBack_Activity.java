package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Prochill_nextback_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Program_Day_User_Child_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.Program_child_Model;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProChil_Inf_NextBack_Activity extends AppCompatActivity {
    private ImageButton nextCurren, backCurren;
    private TextView currenText, totalText;
    private ViewPager chill_inf_main;
    private Button close_info_nb;
    List<Program_child_Model> mList ;
    Long idItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_chil_inf_next_back);
        anhxa();


        currenText.setText("1");

        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idItem = sharedPreferences.getLong("id_idDay",0);
        mList = new ArrayList<>();
        SelecDatabase selecDatabase1 = new SelecDatabase();
        selecDatabase1.execute();
        Toolbar actionBar = findViewById(R.id.toolbarChilNB);
        setToolbar(actionBar, "Infomation Program");
        chill_inf_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currenText.setText(String.valueOf(position+1));
                if (position==0){
                    backCurren.setVisibility(View.GONE);
                    nextCurren.setVisibility(View.VISIBLE);
                } else if (position == mList.size()-1){
                    backCurren.setVisibility(View.VISIBLE);
                    nextCurren.setVisibility(View.GONE);
                }else {
                    backCurren.setVisibility(View.VISIBLE);
                    nextCurren.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        backCurren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chill_inf_main.setCurrentItem(chill_inf_main.getCurrentItem() - 1);
            }
        });
        nextCurren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chill_inf_main.setCurrentItem(chill_inf_main.getCurrentItem() + 1);
            }
        });
        close_info_nb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void anhxa() {
        nextCurren = findViewById(R.id.next_chill_inf);
        backCurren = findViewById(R.id.back_chill_inf);
        currenText = findViewById(R.id.Current_chill_inf);
        totalText = findViewById(R.id.Total_chill_inf);
        chill_inf_main = findViewById(R.id.chill_inf_main);
        close_info_nb = findViewById(R.id.close_info_nb);

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
            mList = new ArrayList<>();
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Day.Day AS DayName, Program_Child.Name AS ProgramChildName, Program_Child.Calo, Program_Child.Unit as ProgramChildUnit, Program_Child.Img, Program_Child.Information as ProgramChildInformation " +
                            "FROM Day " +
                            "JOIN Program_Child ON Day.ID_Day = Program_Child.ID_Day " +
                            "JOIN Program ON Day.ID_Pro = Program.ID_Pro " +
                            "WHERE Day.ID_Day = ?";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setLong(1, idItem);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String name = resultSet.getString("ProgramChildName");
                        String day = resultSet.getString("DayName");
                        String unil = resultSet.getString("ProgramChildUnit");
                        String Inf = resultSet.getString("ProgramChildInformation");
                        Program_child_Model pt = new Program_child_Model(name,day,Long.valueOf(unil) ,Inf);
                        mList.add(pt);
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
            return mList;
        }
        @Override
        protected void onPostExecute(List<Program_child_Model> program_child_Model) {
            super.onPostExecute(program_child_Model);
            if (program_child_Model != null && program_child_Model.size() > 0) {
                Prochill_nextback_Adapter prochillNextbackAdapter = new Prochill_nextback_Adapter(getSupportFragmentManager() , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,program_child_Model);
                chill_inf_main.setAdapter(prochillNextbackAdapter);
                totalText.setText(String.valueOf(program_child_Model.size()));
            } else {
                Toast.makeText(ProChil_Inf_NextBack_Activity.this, "AAAAAA ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}