package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.FoodModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.FoodSelectionListener;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.jdbcConnect.MediaManagerInitializer;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Food_Recycle_Adapter extends RecyclerView.Adapter<Food_Recycle_Adapter.ViewHolder> {
    private List<FoodModel> foodModel;
    private String imageUrl;
    private Set<Integer> selectedPositions = new HashSet<>();
    private Context context;
    private Activity activity;
    ImageView img_add_food_exper_update_pt_inormation;
    private FoodSelectionListener foodSelectionListener;
    public interface FoodSelectionCallback {
        void onFoodItemSelected(int totalCalories);
    }


    public void setData(List<FoodModel> foodList) {
        if (foodList != null) {
            this.foodModel = foodList;
            notifyDataSetChanged();
        }
    }

    public Food_Recycle_Adapter(List<FoodModel> foodList, Activity activity, Context context) {
        this.foodModel = foodList != null ? foodList : new ArrayList<>();
        this.activity = activity;
        this.context = context; // Add this line to initialize context
    }
    public void setFoodSelectionListener(FoodSelectionListener listener) {
        this.foodSelectionListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_recycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodModel item = foodModel.get(position);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        String level = sharedPreferences.getString("levelID","");

        // Assuming that your FoodModel has getters for the properties
        holder.name_food_user_ca.setText(item.getName());
        holder.calo_food_ptIn_User.setText("Calories: " + item.getCalo());
        holder.unil_food_ptIn_User.setText("Unit: " + item.getUnit() + " /g");
        Picasso.get().load(item.getImg()).transform(new CircleTransform()).into(holder.profile_pic_image_view_food);
        if (selectedPositions.contains(position)) {
            holder.layoutBap.setBackgroundResource(R.drawable.rounded_background4); // Change to your desired background resource
        } else {
            holder.layoutBap.setBackgroundResource(R.drawable.rounded_background3); // Change to your default background resource
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(level) == 1 || Integer.parseInt(level) == 2) {
                    showAddDialog(item);
                } else {
                    int adapterPosition = holder.getAdapterPosition();

                    if (selectedPositions.contains(adapterPosition)) {
                        selectedPositions.remove(adapterPosition);
                    } else {
                        selectedPositions.add(adapterPosition);
                    }

                    // Cập nhật giao diện
                    notifyDataSetChanged();

                    // Cập nhật tổng calo
                    int totalCalories = getTotalCaloriesOfSelectedItems();
                    if (foodSelectionListener != null) {
                        foodSelectionListener.onFoodItemSelected(totalCalories);
                    }
                }
            }
        });
    }
    public int getTotalCaloriesOfSelectedItems() {
        int totalCalories = 0;
        for (Integer position : selectedPositions) {
            totalCalories += Integer.parseInt(foodModel.get(position).getCalo());
        }
        return totalCalories;
    }

    private void showAddDialog(FoodModel foodModel) {
        // Sử dụng AlertDialog.Builder để tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add program");

        // Inflate layout cho dialog
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.ptrainner_add_food, null);
        builder.setView(dialogView);

        // Ánh xạ các thành phần trong dialog
        EditText User_Name_food_update_pt_inormation = dialogView.findViewById(R.id.User_Name_food_update_pt_inormation);
        EditText Information_add_food_exper_update_pt_inormation = dialogView.findViewById(R.id.Information_add_food_exper_update_pt_inormation);
        EditText add_pro_food_calo = dialogView.findViewById(R.id.add_pro_food_calo);
        EditText add_pro_food_unil = dialogView.findViewById(R.id.add_pro_food_unil);
        Spinner levelSpinner_session_food = dialogView.findViewById(R.id.levelSpinner_session_food);
        img_add_food_exper_update_pt_inormation = dialogView.findViewById(R.id.img_add_food_exper_update_pt_inormation);

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.session_array, android.R.layout.simple_spinner_item);

        // Đặt layout cho dropdown của Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Đặt ArrayAdapter vào Spinner
        levelSpinner_session_food.setAdapter(adapter);

        String[] sessionOptions = activity.getResources().getStringArray(R.array.session_array);
        User_Name_food_update_pt_inormation.setText(foodModel.getName());
        Information_add_food_exper_update_pt_inormation.setText(foodModel.getInformation());
        add_pro_food_calo.setText(foodModel.getCalo());
        add_pro_food_unil.setText(foodModel.getUnit());
        Picasso.get().load(foodModel.getImg()).into(img_add_food_exper_update_pt_inormation);
        int position = -1;
        for (int i = 0; i < sessionOptions.length; i++) {
            if (foodModel.getSession().equals(sessionOptions[i])) {
                position = i;
                break;
            }
        }

        // Set the selection for the Spinner
        if (position != -1) {
            levelSpinner_session_food.setSelection(position);
        }

        img_add_food_exper_update_pt_inormation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activity.startActivityForResult(intent, 1);
            }
        });

        // Xử lý sự kiện khi người dùng nhấn nút "OK"
        builder.setPositiveButton("Repair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy giá trị từ EditText và Spinner
                String name = User_Name_food_update_pt_inormation.getText().toString();
                String calo = add_pro_food_calo.getText().toString();
                String until = add_pro_food_unil.getText().toString();
                String session = levelSpinner_session_food.getSelectedItem().toString();

                FoodModel updatedFood = new FoodModel();
                updatedFood.setName(name);
                updatedFood.setCalo(calo);
                updatedFood.setUnit(until);
                updatedFood.setSession(session);
                updatedFood.setImg(imageUrl);
                updatedFood.setID_Foo(foodModel.getID_Foo()); // Assuming there is a getID_Foo() method in FoodModel
                Log.d("TAG", "ID_FOO: " + foodModel.getID_Foo());
                // Thực hiện các hành động cập nhật với updatedFood ở đây
                new UpdateFoodInDatabase(foodModel.getID_Foo()).execute(updatedFood);

                // Đóng dialog
                dialog.dismiss();
            }
        });

        // Xử lý sự kiện khi người dùng nhấn nút "Delete"
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện các hành động xóa với foodModel ở đây
                new DeleteFoodFromDatabase().execute(foodModel.getID_Foo());

                // Đóng dialog
                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private class UpdateFoodInDatabase extends AsyncTask<FoodModel, Void, Void> {
        private Exception exception; // Declare an exception variable
        private long foodIdToUpdate; // Variable to store the food ID for update

        // Constructor to receive the food ID when creating the AsyncTask
        public UpdateFoodInDatabase(long foodId) {
            this.foodIdToUpdate = foodId;
        }

        @Override
        protected Void doInBackground(FoodModel... foods) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // SQL query để cập nhật một mục thức ăn
                    String updateQuery = "UPDATE Food SET Name=?, Calo=?, Unit=?, Session=?, Img=? WHERE ID_Foo=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                    // Lấy mục thức ăn từ tham số
                    FoodModel food = foods[0];

                    // Đặt giá trị cho các tham số trong truy vấn
                    preparedStatement.setString(1, food.getName());
                    preparedStatement.setString(2, food.getCalo());
                    preparedStatement.setString(3, food.getUnit());
                    preparedStatement.setString(4, food.getSession());
                    preparedStatement.setString(5, food.getImg());
                    preparedStatement.setLong(6, foodIdToUpdate); // Sử dụng foodIdToUpdate thay vì food.getID_Foo()

                    // Thực thi truy vấn UPDATE
                    preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    this.exception = e; // Assign the exception to the variable
                    e.printStackTrace(); // Log the exception or handle it as appropriate
                } catch (java.sql.SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace(); // Log the exception or handle it as appropriate
                    } catch (java.sql.SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return null;
        }
    }





    private class DeleteFoodFromDatabase extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... foodIds) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // SQL query để xóa một mục thức ăn dựa trên ID
                    String deleteQuery = "DELETE FROM Food WHERE ID_Foo=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);

                    // Đặt giá trị cho tham số trong truy vấn
                    preparedStatement.setLong(1, foodIds[0]);

                    // Thực thi truy vấn DELETE
                    preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (java.sql.SQLException e) {
                    throw new RuntimeException(e);
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
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Tải ảnh lên Clouddinary
            try {
                uploadImageToCloudinary(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(activity.getApplicationContext(), "Đang tải lên...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Xử lý sự thay đổi trong quá trình tải lên
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = (String) resultData.get("secure_url");
                        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(img_add_food_exper_update_pt_inormation);
                        Toast.makeText(activity.getApplicationContext(), "Tải lên thành công!" + imageUrl, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(activity.getApplicationContext(), "Tải lên thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Xử lý khi cần lên lịch lại việc tải lên
                    }
                })
                .option("folder", "android_upload")
                .dispatch();
    }

    @Override
    public int getItemCount() {
        return foodModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_pic_image_view_food;
        public TextView name_food_user_ca;
        public TextView calo_food_ptIn_User, unil_food_ptIn_User;
        public LinearLayout layoutBap;
        public ViewHolder(View itemView) {
            super(itemView);
            profile_pic_image_view_food = itemView.findViewById(R.id.profile_pic_image_view_food);
            name_food_user_ca = itemView.findViewById(R.id.name_food_user_ca);
            calo_food_ptIn_User = itemView.findViewById(R.id.calo_food_ptIn_User);
            unil_food_ptIn_User = itemView.findViewById(R.id.unil_food_ptIn_User);
            layoutBap = itemView.findViewById(R.id.layout_bap);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Lấy vị trí của mục đã nhấp
                    int adapterPosition = getAdapterPosition();

                    // Xử lý sự kiện click cho mục tại vị trí adapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        // Thực hiện các hành động bạn muốn khi một mục được nhấp vào
                        // Ví dụ: mở hộp thoại, chuyển hướng đến màn hình chi tiết, v.v.
                    }
                }
            });
            }

    }
}
