package com.source.tidytimetable.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.source.tidytimetable.*;
import com.source.tidytimetable.main.MainActivity;

public class AddTaskFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));

        return inflater.inflate(R.layout.fragment_addtask, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGray)));
    }

}
