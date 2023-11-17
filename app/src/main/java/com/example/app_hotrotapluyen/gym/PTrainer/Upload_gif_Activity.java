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

import java.util.HashMap;
import java.util.Map;

public class Upload_gif_Activity extends AppCompatActivity {
    private static final int IMAGE_REQ = 1;
    private Uri imagePath;
    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_gif);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dlpqr1jhm");
        config.put("api_key", "187745367395712");
        config.put("api_secret", "-_7wEP5n5Il_4lpiZRm2f1XgAxg");
        MediaManager.init(this, config);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_REQ);
        });
    }

    @Override
    // ...

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQ && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            Picasso.get().load(imagePath).into(imageView);

            // Tạo folder tự động
            String folderName = "ProGram"; // Thay đổi thành tên folder mong muốn

            // Sử dụng Picasso để tải ảnh lên Cloudinary
            MediaManager.get().upload(Uri.parse(imagePath.toString()))
                    .option("folder", folderName)
                    .unsigned("ml_default")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            // Gọi khi quá trình tải lên bắt đầu
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            // Gọi trong quá trình tải lên để báo cáo tiến trình
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            // Gọi khi quá trình tải lên thành công
                            // Lấy thông tin về ảnh đã tải lên từ resultData

                            // Hiển thị thông báo cho người dùng
                            Toast.makeText(Upload_gif_Activity.this, "Ảnh đã được tải lên thành công", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            // Gọi khi có lỗi xảy ra trong quá trình tải lên
                            // Hiển thị thông báo cho người dùng nếu có lỗi
                            Toast.makeText(Upload_gif_Activity.this, "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            // Gọi khi quá trình tải lên cần được lên lịch lại
                        }
                    }).dispatch();
        }
    }

// ...

}
