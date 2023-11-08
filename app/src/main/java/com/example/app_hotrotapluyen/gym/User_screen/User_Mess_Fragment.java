package com.example.app_hotrotapluyen.gym.User_screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.app_hotrotapluyen.gym.until.FirebaseUntil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.example.app_hotrotapluyen.R;

public class User_Mess_Fragment extends Fragment {
    EditText rearchInput;
    ImageButton rearchBtn ;
    RecyclerView recyclerViewMessage;
    Search_UserRecy_MessAdapter searchUserRecyMessAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user__mess_, container, false);
        rearchInput = view.findViewById(R.id.Rearch_Input_MessU);
        rearchBtn = view.findViewById(R.id.rearch_btn_MessU);
        recyclerViewMessage  = view.findViewById(R.id.recyclerViewMessage);
        rearchInput.requestFocus();
        rearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchIN= rearchInput.getText().toString();
                if (searchIN.isEmpty()){
                    rearchInput.setError("Invalid UserName");
                    return;
                }
                setupRecycleview(searchIN);
            }


        });



        return view;
    }
    void setupRecycleview(String searchIN) {

        Query query = FirebaseUntil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("name",searchIN)
                .whereLessThanOrEqualTo("name",searchIN+'\uf8ff');

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query,User.class).build();

        searchUserRecyMessAdapter = new Search_UserRecy_MessAdapter(options,requireContext());
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewMessage.setAdapter(searchUserRecyMessAdapter);
        searchUserRecyMessAdapter.startListening();

    }
    @Override
    public void onStart() {
        super.onStart();
        if(searchUserRecyMessAdapter!=null)
            searchUserRecyMessAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(searchUserRecyMessAdapter!=null)
            searchUserRecyMessAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(searchUserRecyMessAdapter!=null)
            searchUserRecyMessAdapter.startListening();
    }
}