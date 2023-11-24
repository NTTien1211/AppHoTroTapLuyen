package com.example.app_hotrotapluyen.gym.notification;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {

    public void showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveClickListener) {
        // Tạo một đối tượng AlertDialog.Builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Thiết lập tiêu đề và thông điệp cho dialog
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        // Thiết lập nút đồng ý cho dialog
        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện các hành động khi người dùng nhấn nút đồng ý
                dialog.dismiss(); // Đóng dialog sau khi người dùng nhấn nút đồng ý
            }
        });
        alertDialogBuilder.setPositiveButton("OK", positiveClickListener);

        // Hiển thị dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

