package com.viettelpost.remoteconfig.workmanager.ui.main;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viettelpost.remoteconfig.workmanager.R;
import com.viettelpost.remoteconfig.workmanager.worker.MyWorker;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private View view;
    TextView mText;
    private Button btn, btnCancel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mText = view.findViewById(R.id.textView);
        btn = view.findViewById(R.id.simpleWorkButton);
        btnCancel = view.findViewById(R.id.cancelWorkButton);
        Data data = new Data.Builder().putString(MyWorker.EXTRA_TITLE, "My Worker").putString(MyWorker.EXTRA_TEXT, "Hello").build();

        final PeriodicWorkRequest simpleRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 1000, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance().enqueue(simpleRequest);
                WorkManager.getInstance().getStatusByIdLiveData(simpleRequest.getId())
                        .observe(MainFragment.this, new Observer<WorkStatus>() {
                            @Override
                            public void onChanged(@Nullable WorkStatus workStatus) {
                                if (workStatus != null) {
                                    mText.append("SimpleWorkRequest: " + workStatus.getState().name() + "\n");
                                }

                                if (workStatus != null && workStatus.getState().isFinished()) {
                                    String message = workStatus.getOutputData().getString(MyWorker.EXTRA_OUTPUT_MESSAGE);
                                    mText.append("SimpleWorkRequest (Data): " + message);
                                }
                            }
                        });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UUID workId = simpleRequest.getId();
                WorkManager.getInstance().cancelWorkById(workId);
            }
        });
    }

    private Constraints getCon() {
        Constraints constraints = new Constraints.Builder().setRequiresCharging(true).build();
        return constraints;
    }

}
