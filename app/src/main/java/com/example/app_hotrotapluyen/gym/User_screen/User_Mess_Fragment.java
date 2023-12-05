package com.example.app_hotrotapluyen.gym.User_screen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.app_hotrotapluyen.gym.User_screen.Adapter.RecentChat_Mess_Adapter;
import com.example.app_hotrotapluyen.gym.User_screen.Adapter.Search_UserRecy_MessAdapter;
import com.example.app_hotrotapluyen.gym.User_screen.Model.ChatRoomModel;
import com.example.app_hotrotapluyen.gym.User_screen.Model.UserModel;
import com.example.app_hotrotapluyen.gym.until.AndroidUtil;
import com.example.app_hotrotapluyen.gym.until.FirebaseUntil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.example.app_hotrotapluyen.R;

public class User_Mess_Fragment extends Fragment {
    EditText rearchInput;
    ImageButton rearchBtn ;
    RecyclerView recyclerViewMessage , recyclerViewMessage_History;
    Search_UserRecy_MessAdapter searchUserRecyMessAdapter;
    RecentChat_Mess_Adapter recentChatMessAdapter;
    String idUser;
    UserModel otherUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user__mess_, container, false);
        rearchInput = view.findViewById(R.id.Rearch_Input_MessU);
        rearchBtn = view.findViewById(R.id.rearch_btn_MessU);
        recyclerViewMessage  = view.findViewById(R.id.recyclerViewMessage);
        recyclerViewMessage_History  = view.findViewById(R.id.recyclerViewMessage_History);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("GymTien", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("userID","");
        setupRecyclerViewHistory();
//        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        rearchInput.requestFocus();
        rearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchIN= rearchInput.getText().toString();
                if (searchIN.isEmpty()){
                    rearchInput.setError("Invalid UserName");
                    return;
                }
                recyclerViewMessage_History.setVisibility(View.GONE);
                recyclerViewMessage.setVisibility(View.VISIBLE);
                setupRecycleview(searchIN);
            }


        });



        return view;
    }
    void setupRecycleview(String searchIN) {
        if (searchUserRecyMessAdapter != null) {
            searchUserRecyMessAdapter.stopListening();
        }

        Query query = FirebaseUntil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("phone",searchIN)
                .whereLessThanOrEqualTo("phone",searchIN+'\uf8ff');

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();
        if (recentChatMessAdapter != null) {
            // Nếu adapter lịch sử đang được sử dụng, dừng lắng nghe của nó
            recentChatMessAdapter.stopListening();
        }

        searchUserRecyMessAdapter = new Search_UserRecy_MessAdapter(options,requireContext());
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewMessage.setAdapter(searchUserRecyMessAdapter);
        searchUserRecyMessAdapter.startListening();

    }
    void setupRecyclerViewHistory(){
        if (recentChatMessAdapter != null) {
            recentChatMessAdapter.stopListening();
        }
        Query query = FirebaseUntil.allChatroomCollectionReference()
                .whereArrayContains("userIds",idUser)
                .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query,ChatRoomModel.class).build();
        if (recentChatMessAdapter != null) {
            // Nếu adapter lịch sử đang được sử dụng, dừng lắng nghe của nó
            recentChatMessAdapter.stopListening();
        }

        recentChatMessAdapter = new RecentChat_Mess_Adapter(options,getContext() , idUser);
        recyclerViewMessage_History.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMessage_History.setAdapter(recentChatMessAdapter);
        recentChatMessAdapter.startListening();

        recyclerViewMessage.setVisibility(View.GONE);

    }
    @Override
    public void onStart() {
        super.onStart();
        if (searchUserRecyMessAdapter != null) {
            searchUserRecyMessAdapter.startListening();
        }
        if (recentChatMessAdapter != null) {
            recentChatMessAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (searchUserRecyMessAdapter != null) {
            searchUserRecyMessAdapter.stopListening();
        }
        if (recentChatMessAdapter != null) {
            recentChatMessAdapter.stopListening();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (searchUserRecyMessAdapter != null) {
            searchUserRecyMessAdapter.startListening();
        }
    }

}