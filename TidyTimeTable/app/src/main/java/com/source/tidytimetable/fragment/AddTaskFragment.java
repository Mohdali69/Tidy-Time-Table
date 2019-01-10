package com.source.tidytimetable.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View.OnClickListener;
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

public class AddTaskFragment extends Fragment implements View.OnClickListener {

    Button btndeb;
    Button btnfin;
    Button btnvalider;
    TextView textdeb;
    TextView textfin;
    EditText text_nom_tache;
    String nomtache;
    EditText commentaire;
    String comm;
    DatePickerDialog.OnDateSetListener datepick;
    Calendar c;
    String debutText;
    String finText;
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
        debutText = textdeb.getText().toString();
        finText = textfin.getText().toString();



        btnfin.setOnClickListener(this);
        btndeb.setOnClickListener(this);
        btnvalider.setOnClickListener(this);

        return inflater.inflate(R.layout.fragment_addtask, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_debut:
                c= Calendar.getInstance();
                int day =c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year= c.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datepick,
                        year,month,day);


                dialog.show();
                datepick = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textdeb.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                };


            case R.id.btn_fin:
                c= Calendar.getInstance();
                day =c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year= c.get(Calendar.YEAR);
                dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datepick,
                        year,month,day);


                dialog.show();
                datepick = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textfin.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                };

            case R.id.btn_valider:
                if(debutText=="Date Debut"){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré de date de debut",Toast.LENGTH_LONG).show();
                }
                else if(finText=="Date FIN"){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré de date de fin",Toast.LENGTH_LONG).show();

                }
                else if(nomtache.matches("")){
                    Toast.makeText(getActivity(), "Vous n'avez pas entré le nom de la tâche", Toast.LENGTH_LONG).show();

                }
                else if(comm.matches("")){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré de commentaire",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getActivity(),"Tout est bon",Toast.LENGTH_LONG).show();//Entrez des Valeurs dans la base de donnés
                }

        }
    }
}
