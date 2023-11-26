package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.CircleTransform;
import com.example.app_hotrotapluyen.gym.User_screen.Model.DayPRo_model;
import com.example.app_hotrotapluyen.gym.User_screen.Model.HomeU_pt;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ProgramModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.Program_child_Model;
import com.example.app_hotrotapluyen.gym.User_screen.ProChil_Inf_NextBack_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_Mess_Chat_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_PT_Inf_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_listPro_Day_Activity;
import com.example.app_hotrotapluyen.gym.User_screen.User_listPro_ProgramChild_Activity;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.until.AndroidUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program_Day_User_Child_Adapter extends RecyclerView.Adapter<Program_Day_User_Child_Adapter.ViewHolder> {
    private List<Program_child_Model> program_child_Model;
    private Context context;
    private Activity activity;
    private String imageUrl;
    ImageView img_add_child_exper_update_pt_inormation;
    public Program_Day_User_Child_Adapter(List<Program_child_Model> program_child_Model , Activity activity) {
        this.program_child_Model = program_child_Model;
        this.activity = activity;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycl_lispro_childay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program_child_Model item = program_child_Model.get(position);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        String level = sharedPreferences.getString("levelID","");
        holder.listpro_dayChild_name.setText(item.getNameProChi());
        String url = item.getImg();
        Log.d("TAG", "onBindViewHolder: " +url);
        Glide.with(holder.itemView.getContext())
                .load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.person_icon)) // Placeholder image while loading
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.img_proChild);

        holder.listpro_dayChiil_unil.setText(String.valueOf(item.getUnil()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(level) == 0){
                    Intent intent = new Intent(view.getContext(), ProChil_Inf_NextBack_Activity.class);
                    view.getContext().startActivity(intent);
                }else {
                    showAddDialog(item);
                }

            }
        });
    }
    private void showAddDialog(Program_child_Model item) {
        // Sử dụng AlertDialog.Builder để tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add program");


        // Inflate layout cho dialog
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.ptrainner_add_pro_child, null);
        builder.setView(dialogView);
//        dialogView.setBackgroundResource(R.drawable.rounded_background2);

        // Ánh xạ các thành phần trong dialog
        final EditText User_Name_exper_update_pt_inormation = dialogView.findViewById(R.id.User_Name_exper_update_pt_inormation_dialog);
        final EditText Information_add_child_exper_update_pt_inormation = dialogView.findViewById(R.id.Information_add_child_exper_update_pt_inormation);
        final EditText add_pro_chill_calo = dialogView.findViewById(R.id.add_pro_chill_calo);
        final EditText add_pro_chill_unil = dialogView.findViewById(R.id.add_pro_chill_unil);
        img_add_child_exper_update_pt_inormation = dialogView.findViewById(R.id.img_add_child_exper_update_pt_inormation);


        img_add_child_exper_update_pt_inormation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activity.startActivityForResult(intent, 1);
            }
        });

        User_Name_exper_update_pt_inormation.setText(String.valueOf(item.getNameProChi()));
        Information_add_child_exper_update_pt_inormation.setText(item.getInfomat());
        add_pro_chill_calo.setText(item.getCalo());
        add_pro_chill_unil.setText(String.valueOf(item.getUnil()));
        Picasso.get().load(item.getImg()).into(img_add_child_exper_update_pt_inormation);
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
                programModel.setID_Program_Child((int) item.getID_Program_Child());
                new AddDayToDatabase(item.getID_Program_Child()).execute(programModel);
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
                new DeleteFoodFromDatabase().execute(item.getID_Program_Child());
                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private class DeleteFoodFromDatabase extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... foodIds) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    // SQL query để xóa một mục thức ăn dựa trên ID
                    String deleteQuery = "DELETE FROM Program WHERE ID_Pro=?";
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
    private class AddDayToDatabase extends AsyncTask<Program_child_Model, Void, Void> {
        private long foodIdToUpdate; // Variable to store the food ID for update

        // Constructor to receive the food ID when creating the AsyncTask
        public AddDayToDatabase(long foodId) {
            this.foodIdToUpdate = foodId;
        }
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
            String updateQuery = "UPDATE Program_Child " +
                    "SET Name = ?, Calo = ?, Information = ?, Img = ?, Unit = ? " +
                    "WHERE ID_Day = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, programChild.getNameProChi());
                preparedStatement.setString(2, programChild.getCalo());
                preparedStatement.setString(3, programChild.getInfomat());
                preparedStatement.setString(4, programChild.getImg());
                preparedStatement.setString(5, String.valueOf(programChild.getUnil()));
                preparedStatement.setLong(6, foodIdToUpdate);
                preparedStatement.executeUpdate();
        } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
            }
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
                        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(img_add_child_exper_update_pt_inormation);
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
        return program_child_Model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_proChild;
        TextView listpro_dayChild_name, listpro_dayChiil_unil;
        
        public ViewHolder(View itemView) {
            super(itemView);

            img_proChild = itemView.findViewById(R.id.img_proChild);
            listpro_dayChild_name = itemView.findViewById(R.id.listpro_dayChild_name);
            listpro_dayChiil_unil = itemView.findViewById(R.id.listpro_dayChiil_unil);


        }

    }
}

