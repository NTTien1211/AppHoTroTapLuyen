package com.example.app_hotrotapluyen.gym.User_screen.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ProgramViewModel extends ViewModel {
    private MutableLiveData<List<ProgramModel>> programModelList = new MutableLiveData<>();

    public void setProgramModelList(List<ProgramModel> programModels) {
        programModelList.setValue(programModels);
    }

    public LiveData<List<ProgramModel>> getProgramModelList() {
        return programModelList;
    }
}
