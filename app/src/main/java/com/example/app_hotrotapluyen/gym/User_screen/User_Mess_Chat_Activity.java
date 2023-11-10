package com.example.app_hotrotapluyen.gym.User_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.ChatRecyclerAdapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ChatMessModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ChatRoomModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.jdbcConnect.JdbcConnect;
import com.example.app_hotrotapluyen.gym.until.AndroidUtil;
import com.example.app_hotrotapluyen.gym.until.FirebaseUntil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class User_Mess_Chat_Activity extends AppCompatActivity {
    String idUser,userName, userPhone;
    TextView profile_Username_chatMess;
    EditText chat_message_input;
    String ChatRoomID;
    ImageButton backChat, sendChat;
    RecyclerView recyclerView;
    ChatRoomModel chatroomModel;
    UserModel otherUser;
    ChatRecyclerAdapter chatRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mess_chat);
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        SharedPreferences sharedPreferences = getSharedPreferences("GymTien",MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");

        ChatRoomID = FirebaseUntil.getChatroomId(idUser,otherUser.getIdUser());
        anhxa();


        SelecDatabase selecDatabase = new SelecDatabase();
        selecDatabase.execute(idUser);

        backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
            }
        });
        profile_Username_chatMess.setText(otherUser.getName());
        getOrCreateChatroomModel();

        sendChat.setOnClickListener((v -> {
            String message = chat_message_input.getText().toString().trim();
            Toast.makeText(this, "qfqfqfqf "+message, Toast.LENGTH_SHORT).show();
            if(message.isEmpty()){
                return;
            }
            else {
                sendMessToUser(message);
            }

        }));


        setupChatRecyclerView();




    }

    void setupChatRecyclerView(){
        Query query = FirebaseUntil.getChatroomMessageReference(ChatRoomID)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessModel> options = new FirestoreRecyclerOptions.Builder<ChatMessModel>()
                .setQuery(query,ChatMessModel.class).build();

        chatRecyclerAdapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatRecyclerAdapter);
        chatRecyclerAdapter.startListening();
        chatRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessToUser(String mess) {
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(idUser);
        FirebaseUntil.getChatroomReference(ChatRoomID).set(chatroomModel);

        ChatMessModel chatMessModel = new ChatMessModel(mess, idUser,Timestamp.now());
        Toast.makeText(this, " " + mess + " " + idUser + " " +Timestamp.now(), Toast.LENGTH_SHORT).show();
        // Cập nhật mô hình phòng chat và sau đó gửi tin nhắn
        FirebaseUntil.getChatroomMessageReference(ChatRoomID).add(chatMessModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            chat_message_input.setText("");

                        }
                    }
                });
    }

    void getOrCreateChatroomModel(){
        FirebaseUntil.getChatroomReference(ChatRoomID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatroomModel = task.getResult().toObject(ChatRoomModel.class);
                if(chatroomModel==null){
                    //first time chat
                    chatroomModel = new ChatRoomModel(
                            ChatRoomID,
                            Arrays.asList(idUser, otherUser.getIdUser()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUntil.getChatroomReference(ChatRoomID).set(chatroomModel);
                }
            }
        });
    }
    private void anhxa() {
        profile_Username_chatMess = findViewById(R.id.profile_Username_chatMess);
        chat_message_input = findViewById(R.id.chat_message_input);
        backChat = findViewById(R.id.back_chatMessU);
        sendChat = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.Recycl_chatMessU);



    }

    private class  SelecDatabase extends AsyncTask<String, Void, UserModel> {

        @Override
        protected UserModel doInBackground(String... strings) {
            Connection connection = JdbcConnect.connect();
            if (connection != null) {
                try {
                    String query = "SELECT Name, Phone FROM Users WHERE Id_User = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1,idUser );

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String name = resultSet.getString("Name");
                        String phone = resultSet.getString("Phone");

                        return new UserModel(idUser, name, phone);
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
            return null;
        }
        @Override
        protected void onPostExecute(UserModel result) {
            super.onPostExecute(result);
            if (result!= null) {

            } else {
                Toast.makeText(User_Mess_Chat_Activity.this, "User not found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }


}