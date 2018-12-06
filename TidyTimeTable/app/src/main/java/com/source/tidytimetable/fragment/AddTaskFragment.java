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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.source.tidytimetable.*;
import com.source.tidytimetable.main.MainActivity;

import java.util.Calendar;

public class AddTaskFragment extends Fragment {

    Button btndeb;
    Button btnfin;
    Button btnvalider;
    TextView textdeb;
    TextView textfin;
    EditText text_nom_tache;
    String nomtache;
    EditText commentaire;
    String comm;
    DatePickerDialog datepick;
    Calendar c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_addtask, container, false);
        btndeb = v.findViewById(R.id.btn_debut);
        btnfin = v.findViewById(R.id.btn_fin);
        textdeb = v.findViewById(R.id.text_debut);
        textfin = v.findViewById(R.id.text_fin);
        btnvalider = v.findViewById(R.id.btn_valider);
        text_nom_tache = v.findViewById(R.id.text_nom_tache);
        commentaire= v.findViewById(R.id.text_commentaire);
        comm=commentaire.getText().toString();
        nomtache=text_nom_tache.getText().toString();
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
        btndeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textdeb.getText().toString()=="Date Debut"){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré de date de debut",Toast.LENGTH_LONG).show();
                }
                else if(textfin.getText().toString()=="Date FIN"){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré de date de fin",Toast.LENGTH_LONG).show();

                }
                else if(nomtache.matches("")){
                    Toast.makeText(getActivity(), "Vous n'avez pas entré le nom de la tâche", Toast.LENGTH_LONG).show();

                }
                else if(comm.matches("")){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré de commentaire",Toast.LENGTH_LONG).show();

                }
                else{
                    //Entrez des Valeurs dans la base de donnés
                }
            }
        });

        return inflater.inflate(R.layout.fragment_addtask, container, false);
    }


    public void onDestroy() {
        super.onDestroy();
        MainActivity.fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGray)));
    }

}
