package com.source.tidytimetable.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.source.tidytimetable.*;
import com.source.tidytimetable.main.MainActivity;

import java.util.Calendar;

public class AddTaskFragment extends Fragment {

    Button btndeb;
    Button btnfin;
    TextView textdeb;
    TextView textfin;
    DatePickerDialog datepick;
    Calendar c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_addtask, container, false);
        btndeb = (Button)v.findViewById(R.id.btn_debut);
        btnfin = (Button)v.findViewById(R.id.btn_fin);
        textdeb = (TextView)v.findViewById(R.id.text_debut);
        textfin = (TextView)v.findViewById(R.id.text_fin);
        btndeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c= Calendar.getInstance();
                int day =c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year= c.get(Calendar.YEAR);
                datepick = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        textdeb.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                },day,month,year);
                datepick.show();
            }
        });
        btnfin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c= Calendar.getInstance();
                int day =c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year= c.get(Calendar.YEAR);
                datepick = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        textfin.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                },day,month,year);
                datepick.show();
            }
        });
        return inflater.inflate(R.layout.fragment_addtask, container, false);
    }


    public void onDestroy() {
        super.onDestroy();
        MainActivity.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGray)));
    }

}
