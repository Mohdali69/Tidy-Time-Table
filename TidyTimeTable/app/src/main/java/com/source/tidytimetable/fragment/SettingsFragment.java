package com.source.tidytimetable.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.source.tidytimetable.*;
import com.source.tidytimetable.main.MainActivity;

public class SettingsFragment extends Fragment {

    private AppCompatButton btn_logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_settings, container, false);

        btn_logout = (AppCompatButton) v.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.logout();
            }
        });

        return v;
    }
}
