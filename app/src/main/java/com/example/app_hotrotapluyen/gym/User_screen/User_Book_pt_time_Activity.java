package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.Map;

public class User_Book_pt_time_Activity extends AppCompatActivity {
    RadioGroup radioGroup1 , radioGroup2;
    String selectedText , timetext;
    Button btnGetTime;
    ImageView imageView;
    TextView moneyTotal;
    String idUser, idPt , money;
    double summoeny;
    int tn = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_pt_time);
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        idPt = getIntent().getStringExtra("idPtss");
        money = getIntent().getStringExtra("moneybook");
        Toolbar actionBar = findViewById(R.id.toolbar_book);
        setToolbar(actionBar, "PT Check Book");
        anhxa();
        Calendar currentDate = Calendar.getInstance();
        new CheckBookedTimeTask().execute(idPt);
        // Thêm 6 tháng


        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = findViewById(checkedId);
                if (checkedRadioButton != null) {
                    timetext = checkedRadioButton.getText().toString();
                }
                else {
                    Toast.makeText(User_Book_pt_time_Activity.this, "Please chose time", Toast.LENGTH_SHORT).show();

                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = findViewById(checkedId);
                if (checkedRadioButton != null) {

                    selectedText = checkedRadioButton.getText().toString();
                    if (selectedText.equals("6 tháng")) tn = 6;
                    else if (selectedText.equals("1 năm")) tn = 12;
                    else if (selectedText.equals("2 năm")) tn = 24;
                    currentDate.add(Calendar.MONTH,tn);

                    // Lấy ngày trong tương lai
                    long futureDateInMillis = currentDate.getTimeInMillis();

                    // Lấy ngày hiện tại
                    long currentDateInMillis = Calendar.getInstance().getTimeInMillis();

                    // Tính số ngày giữa hai ngày
                    long daysBetween = (futureDateInMillis - currentDateInMillis) / (24 * 60 * 60 * 1000);
                    summoeny = Double.parseDouble(String.valueOf(daysBetween)) * Double.parseDouble(money) ;
                    moneyTotal.setText(String.format("%.1f", summoeny));
                }
                else {
                    Toast.makeText(User_Book_pt_time_Activity.this, "Please chose duration", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Bitmap qrCodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode);
        imageView.setImageBitmap(qrCodeBitmap);

        // Convert the Bitmap to a QR code string
        String qrCodeValue = decodeQRCode(qrCodeBitmap);

        if (qrCodeValue != null) {
            Log.d("QR Code", "Decoded value: " + qrCodeValue);
        } else {
            Log.d("QR Code", "Failed to decode QR code");
        }
        btnGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timetext == null) {
                    Toast.makeText(User_Book_pt_time_Activity.this, "Please choose time", Toast.LENGTH_SHORT).show();
                } else if (selectedText == null) {
                    Toast.makeText(User_Book_pt_time_Activity.this, "Please choose duration", Toast.LENGTH_SHORT).show();
                } else {
                    new BookPTTask().execute(idPt, idUser, String.valueOf(summoeny), timetext, String.valueOf(tn));
                }
            }
        });
    }
    private String decodeQRCode(Bitmap bitmap) {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap)));

        try {
            Result result = multiFormatReader.decodeWithState(binaryBitmap);
            return result.getText();
        } catch (ReaderException e) {
            // Handle exception
            Log.e("QR Code", "Error decoding QR code", e);
        } finally {
            multiFormatReader.reset();
        }

        return null;
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
    private static class BitmapLuminanceSource extends com.google.zxing.LuminanceSource {
        private byte[] luminances;

        private BitmapLuminanceSource(Bitmap bitmap) {
            super(bitmap.getWidth(), bitmap.getHeight());
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            luminances = new byte[width * height];

            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            // Convert ARGB to grayscale
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    luminances[y * width + x] = (byte) (pixel & 0xFF);
                }
            }
        }

        @Override
        public byte[] getRow(int y, byte[] row) {
            System.arraycopy(luminances, y * getWidth(), row, 0, getWidth());
            return row;
        }

        @Override
        public byte[] getMatrix() {
            return luminances;
        }

        @Override
        public boolean isCropSupported() {
            return true;
        }

        @Override
        public com.google.zxing.LuminanceSource crop(int left, int top, int width, int height) {
            // Perform cropping, if necessary
            // You can implement this method to return a new LuminanceSource with the cropped region
            return this;
        }

        @Override
        public boolean isRotateSupported() {
            return true;
        }

        @Override
        public com.google.zxing.LuminanceSource rotateCounterClockwise() {
            // Perform rotation, if necessary
            // You can implement this method to return a new LuminanceSource with the rotated data
            return this;
        }
    }
//
    private class BookPTTask extends AsyncTask<String, Void, Boolean> {

        // Hàm kiểm tra xem dữ liệu đã tồn tại hay không
        private boolean isDataExists(String idPT, String idUser) throws SQLException {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT COUNT(*) FROM Book WHERE ID_User_Give = ? AND ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idPT);
                    preparedStatement.setString(2, idUser);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    return count > 0;
                } finally {
                    connection.close();
                }
            }
            return false;
        }

        // Hàm thực hiện cập nhật dữ liệu
        private boolean updateData(String idPT, String idUser, double money, String timeday, int duration) throws SQLException {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    String query = "UPDATE Book SET Time = ?, Money = ?, Status = 'waiting', timein_day = ?, duration = ? WHERE ID_User_Give = ? AND ID_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setTimestamp(1, currentTime);
                    preparedStatement.setDouble(2, money);
                    preparedStatement.setString(3, timeday);
                    preparedStatement.setInt(4, duration);
                    preparedStatement.setString(5, idPT);
                    preparedStatement.setString(6, idUser);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                } finally {
                    connection.close();
                }
            }
            return false;
        }

        // Hàm thực hiện thêm mới dữ liệu
        private boolean insertData(String idPT, String idUser, double money, String timeday, int duration) throws SQLException {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    String query = "INSERT INTO Book (ID_User_Give, Time, Money, Status, timein_day, duration, ID_User) VALUES (?, ?, ?, 'waiting', ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, idPT);
                    preparedStatement.setTimestamp(2, currentTime);
                    preparedStatement.setDouble(3, money);
                    preparedStatement.setString(4, timeday);
                    preparedStatement.setInt(5, duration);
                    preparedStatement.setString(6, idUser);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                } finally {
                    connection.close();
                }
            }
            return false;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                String idPT = strings[0];
                String idUser = strings[1];
                double money = Double.parseDouble(strings[2]);
                String timeday = strings[3];
                int duration = Integer.parseInt(strings[4]);

                if (isDataExists(idPT, idUser)) {
                    return updateData(idPT, idUser, money, timeday, duration);
                } else {
                    return insertData(idPT, idUser, money, timeday, duration);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("TAG", "prinrrrr: " + e);
            }
            return false;
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(User_Book_pt_time_Activity.this, "Success", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(User_Book_pt_time_Activity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class CheckBookedTimeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String idPT = strings[0];

                Connection connection = JdbcConnect.connect();
                if (connection != null) {
                    try {
                        String query = "SELECT timein_day FROM Book WHERE ID_User_Give = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, idPT);

                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            return resultSet.getString("timein_day");
                        }
                    } finally {
                        connection.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("TAG", "Error checking booked time: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String bookedTime) {
            if (bookedTime != null) {
                // Set the corresponding RadioButton in radioGroup1 as checked and change its color to red
                for (int i = 0; i < radioGroup1.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroup1.getChildAt(i);
                    if (radioButton.getText().toString().equals(bookedTime)) {
                        radioButton.setChecked(true);
                        radioButton.setBackgroundResource(R.drawable.rounded_background);
                    }
                }
            }
        }
    }
    private void anhxa() {
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        imageView = findViewById(R.id.imageView);
        btnGetTime = findViewById(R.id.btnGetTime);
        moneyTotal = findViewById(R.id.moneyTotal);

    }
}