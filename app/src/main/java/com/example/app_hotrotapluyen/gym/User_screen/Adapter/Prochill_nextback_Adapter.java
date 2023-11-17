package com.example.app_hotrotapluyen.gym.User_screen.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.app_hotrotapluyen.gym.User_screen.Model.Program_child_Model;
import com.example.app_hotrotapluyen.gym.User_screen.Prochill_inf_Fragment;

import java.util.List;

public class Prochill_nextback_Adapter extends FragmentStatePagerAdapter {

    List<Program_child_Model> programChildModels;

    public Prochill_nextback_Adapter(@NonNull FragmentManager fm, int behavior ,List<Program_child_Model> programChildModels)  {
        super(fm, behavior);
        this.programChildModels = programChildModels;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (programChildModels ==null || programChildModels.isEmpty()){

        }
        Program_child_Model child_ing = programChildModels.get(position);
        Prochill_inf_Fragment prochillInfFragment = new Prochill_inf_Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataChild" ,child_ing );
        prochillInfFragment.setArguments(bundle);
        return prochillInfFragment;
    }

    @Override
    public int getCount() {
        if (programChildModels != null){
            return  programChildModels.size();
        }
        return 0;
    }
}
