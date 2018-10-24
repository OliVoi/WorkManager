package com.viettelpost.remoteconfig.workmanager.ui.main;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.work.WorkStatus;

public class MainViewModel extends ViewModel {
    private MutableLiveData<WorkStatus> name;
    private static MainViewModel mainViewModel;

    public MainViewModel() {
        name = new MutableLiveData<>();
    }

    public LiveData<WorkStatus> getName() {
        return name;
    }



}
