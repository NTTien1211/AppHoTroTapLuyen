package com.example.app_hotrotapluyen.gym.User_screen;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Food_Recycle_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.FoodModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.jdbcConnect.MediaManagerInitializer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_listProFoo_Food_Fragment extends Fragment {
    RecyclerView recyclerViewMorning, recyclerViewNoon, recyclerViewNight;
    Food_Recycle_Adapter adapterMorning, adapterNoon, adapterNight;
    String level , idUser;
    String imageUrl;
    FloatingActionButton add_food_pt;
    ImageView img_add_food_exper_update_pt_inormation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list_pro_foo__food_, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        MediaManagerInitializer.initializeMediaManager(getContext());
        level = sharedPreferences.getString("levelID" ,"");
        idUser = sharedPreferences.getString("userID" ,"");
        recyclerViewMorning = view.findViewById(R.id.recycle_food_morning);
        recyclerViewNoon = view.findViewById(R.id.recycle_food_noon);
        recyclerViewNight = view.findViewById(R.id.recycle_food_night);
        add_food_pt = view.findViewById(R.id.add_food_pt);

        LinearLayoutManager layoutManagerMorning = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerNoon = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerNight = new LinearLayoutManager(getActivity());

        recyclerViewMorning.setLayoutManager(layoutManagerMorning);
        recyclerViewNoon.setLayoutManager(layoutManagerNoon);
        recyclerViewNight.setLayoutManager(layoutManagerNight);

        SelectFoodFromDatabase selectFoodFromDatabase = new SelectFoodFromDatabase();
        try {
            List<FoodModel> foodList = selectFoodFromDatabase.execute().get();

            List<FoodModel> morningList = filterBySession(foodList, "morning");
            List<FoodModel> noonList = filterBySession(foodList, "noon");
            List<FoodModel> nightList = filterBySession(foodList, "night");

            adapterMorning = new Food_Recycle_Adapter(morningList , getActivity());
            adapterNoon = new Food_Recycle_Adapter(noonList,getActivity());
            adapterNight = new Food_Recycle_Adapter(nightList,getActivity());

            recyclerViewMorning.setAdapter(adapterMorning);
            recyclerViewNoon.setAdapter(adapterNoon);
            recyclerViewNight.setAdapter(adapterNight);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(level)  == 1|| Integer.parseInt(level)  == 2){
            add_food_pt.setVisibility(View.VISIBLE);
            add_food_pt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hiển thị dialog thêm name và level
                    showAddDialog(getContext());

                }
            });

        }else {
            add_food_pt.setVisibility(View.GONE);
        }

        return view;
    }
    private void showAddDialog(Context context) {
        // Sử dụng AlertDialog.Builder để tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add program");


        // Inflate layout cho dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.ptrainner_add_food, null);
        builder.setView(dialogView);
//        dialogView.setBackgroundResource(R.drawable.rounded_background2);

        // Ánh xạ các thành phần trong dialog
         EditText User_Name_food_update_pt_inormation = dialogView.findViewById(R.id.User_Name_food_update_pt_inormation);
         EditText Information_add_food_exper_update_pt_inormation = dialogView.findViewById(R.id.Information_add_food_exper_update_pt_inormation);
         EditText add_pro_food_calo = dialogView.findViewById(R.id.add_pro_food_calo);
         EditText add_pro_food_unil = dialogView.findViewById(R.id.add_pro_food_unil);
         Spinner levelSpinner_session_food = dialogView.findViewById(R.id.levelSpinner_session_food);
         img_add_food_exper_update_pt_inormation = dialogView.findViewById(R.id.img_add_food_exper_update_pt_inormation);
        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.session_array, android.R.layout.simple_spinner_item);

        // Đặt layout cho dropdown của Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Đặt ArrayAdapter vào Spinner
        levelSpinner_session_food.setAdapter(adapter);
        img_add_food_exper_update_pt_inormation.setOnClickListener(new View.OnClickListener() {
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
                String name = User_Name_food_update_pt_inormation.getText().toString();
                String calo = add_pro_food_calo.getText().toString();
                String until = add_pro_food_unil.getText().toString();
                String session = levelSpinner_session_food.getSelectedItem().toString();

                FoodModel foodModel = new FoodModel();
                foodModel.setName(name);
                foodModel.setCalo(calo);
                foodModel.setUnit(until);
                foodModel.setSession(session);
                foodModel.setImg(imageUrl);
                new AddFoodToDatabase().execute(foodModel);
                // Thực hiện các hành động với name và selectedLevel ở đây

                // Đóng dialog
                dialog.dismiss();
                loaddata();
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

    private class AddFoodToDatabase extends AsyncTask<FoodModel, Void, Void> {

        @Override
        protected Void doInBackground(FoodModel... foods) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // SQL query to add a new food item
                    String insertQuery = "INSERT INTO Food (Name, Calo, Unit, Session, Img, ID_User) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                    // Get the food item from the parameter
                    FoodModel food = foods[0];

                    // Set values for parameters in the query
                    preparedStatement.setString(1, food.getName());
                    preparedStatement.setString(2, food.getCalo());
                    preparedStatement.setString(3, food.getUnit());
                    preparedStatement.setString(4, food.getSession());
                    preparedStatement.setString(5, food.getImg());
                    preparedStatement.setLong(6, Long.parseLong(idUser)); // Assuming idUser is a class variable

                    // Execute the INSERT query
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

    private void loaddata() {
        SelectFoodFromDatabase selecDatabase1 = new SelectFoodFromDatabase();
        selecDatabase1.execute();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Tải ảnh lên Clouddinary
            try {
                uploadImageToCloudinary(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Đang tải lên...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Xử lý sự thay đổi trong quá trình tải lên
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = (String) resultData.get("secure_url");
                        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(img_add_food_exper_update_pt_inormation);
                        Toast.makeText(getContext(), "Tải lên thành công!" + imageUrl, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(getContext(), "Tải lên thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Xử lý khi cần lên lịch lại việc tải lên
                    }
                })
                .option("folder", "android_upload")
                .dispatch();
    }
    private List<FoodModel> filterBySession(List<FoodModel> foodList, String session) {
        List<FoodModel> filteredList = new ArrayList<>();
        for (FoodModel food : foodList) {
            if (food.getSession().equalsIgnoreCase(session)) {
                filteredList.add(food);
            }
        }
        return filteredList;
    }

    private class SelectFoodFromDatabase extends AsyncTask<String, Void, List<FoodModel>> {
        List<FoodModel> foodList = new ArrayList<>();

        @Override
        protected List<FoodModel> doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT * FROM Food";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        Long id = resultSet.getLong("ID_Foo");
                        String name = resultSet.getString("Name");
                        String calo = resultSet.getString("Calo");
                        String unit = resultSet.getString("Unit");
                        String session = resultSet.getString("Session");
                        String img = resultSet.getString("Img");

                        FoodModel food = new FoodModel();
                        food.setID_Foo(id);
                        food.setName(name);
                        food.setCalo(calo);
                        food.setUnit(unit);
                        food.setSession(session);
                        food.setImg(img);

                        foodList.add(food);
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
            return foodList;
        }
    }
}
