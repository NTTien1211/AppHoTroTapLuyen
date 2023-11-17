package com.example.app_hotrotapluyen.gym.User_screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_hotrotapluyen.R;
import com.example.app_hotrotapluyen.gym.User_screen.Model.Program_child_Model;


public class Prochill_inf_Fragment extends Fragment {
    TextView tv_question,inf_chill_pr;
    ImageView im_child_pr ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_prochill_inf_, container, false);
        tv_question = view.findViewById(R.id.tv_question);
        im_child_pr = view.findViewById(R.id.im_child_pr);
        inf_chill_pr = view.findViewById(R.id.inf_chill_pr);

        Bundle bundle = getArguments();
        if (bundle!=null){
            Program_child_Model program_child_Model = (Program_child_Model) bundle.get("dataChild");
            if (program_child_Model!=null){
                tv_question.setText(program_child_Model.getNameProChi());
                inf_chill_pr.setText(program_child_Model.getInfomat());
            }
        }

        return view;
    }
}