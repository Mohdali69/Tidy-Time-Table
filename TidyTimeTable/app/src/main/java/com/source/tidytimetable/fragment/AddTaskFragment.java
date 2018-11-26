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
                DialogFragment newFrag = new DatePickerFragment();
                newFrag.show(getFragmentManager(),"Date Picker");
            }
        });
        return inflater.inflate(R.layout.fragment_addtask, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGray)));
    }
    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //Do something with the date chosen by the user
            TextView tv = (TextView) getActivity().findViewById(R.id.text_debut);
            tv.setText("Date changed...");
            tv.setText(tv.getText() + "\nYear: " + year);
            tv.setText(tv.getText() + "\nMonth: " + month);
            tv.setText(tv.getText() + "\nDay of Month: " + day);

            String stringOfDate = day + "/" + month + "/" + year;
            tv.setText(tv.getText() + "\n\nFormatted date: " + stringOfDate);
        }
    }

}
