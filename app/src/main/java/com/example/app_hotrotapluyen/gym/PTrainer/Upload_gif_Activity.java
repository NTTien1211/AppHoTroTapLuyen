package com.example.app_hotrotapluyen.gym.PTrainer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.app_hotrotapluyen.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Upload_gif_Activity extends AppCompatActivity {

    private Button uploadButton;
    private ImageView uploadedImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_gif);

        // Khởi tạo Cloudinary (thay thế bằng cloud_name, api_key, và api_secret thực của bạn)
        Map config = new HashMap();
        config.put("cloud_name", "dlpqr1jhm");
        config.put("api_key", "187745367395712");
        config.put("api_secret", "-_7wEP5n5Il_4lpiZRm2f1XgAxg");
        MediaManager.init(this, config);

        // Ánh xạ các thành phần giao diện
        uploadButton = findViewById(R.id.button);
        uploadedImageView = findViewById(R.id.imageView);

        // Thiết lập sự kiện khi nhấn nút tải lên
        uploadButton.setOnClickListener(view -> {
            // Mở Intent để chọn ảnh từ thư viện
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Tải ảnh lên Clouddinary
            try {
                uploadImageToCloudinary(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Upload_gif_Activity.this, "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Upload_gif_Activity.this, "Đang tải lên...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Xử lý sự thay đổi trong quá trình tải lên
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = (String) resultData.get("secure_url");
                        Picasso.get().load(imageUrl).into(uploadedImageView);
                        Toast.makeText(Upload_gif_Activity.this, "Tải lên thành công!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(Upload_gif_Activity.this, "Tải lên thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Xử lý khi cần lên lịch lại việc tải lên
                    }
                })
                .option("folder", "android_upload")
                .dispatch();
    }
}
