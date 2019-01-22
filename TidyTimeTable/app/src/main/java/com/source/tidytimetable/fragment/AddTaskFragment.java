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
import android.support.v7.widget.AppCompatButton;
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
import com.source.tidytimetable.fragment.BackgroudAddTask;

import com.source.tidytimetable.*;
import com.source.tidytimetable.connection.BackgroundRegister;
import com.source.tidytimetable.main.MainActivity;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;

public class AddTaskFragment extends Fragment {


    EditText txtdeb;
    EditText txtfin;
    private AppCompatButton btnvalider;
    TextView textdeb;
    TextView textfin;
    EditText text_nom_tache;
    String nomtache;
    EditText commentaire;
    String comm;

    String debutText;
    String finText;
    Button test;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_addtask, container, false);

        txtdeb = v.findViewById(R.id.text_deb);
        txtfin = v.findViewById(R.id.text_finish);
        textdeb = v.findViewById(R.id.text_debut);
        textfin = v.findViewById(R.id.text_fin);
        btnvalider = (AppCompatButton) v.findViewById(R.id.btn_valider);
        text_nom_tache = v.findViewById(R.id.text_nom_tache);
        commentaire= v.findViewById(R.id.text_commentaire);
        comm=commentaire.getText().toString();
        nomtache=text_nom_tache.getText().toString();
        debutText = txtdeb.getText().toString();
        finText = txtfin.getText().toString();

        System.out.println("zob1");
        btnvalider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("zob");
                if(text_nom_tache.getText().toString().matches("")){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré le nom de la tâche",Toast.LENGTH_LONG).show();
                }
                else if(txtdeb.getText().toString().matches("")){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré de date de debut",Toast.LENGTH_LONG).show();

                }
                else if(txtfin.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Vous n'avez pas entré de date de fin", Toast.LENGTH_LONG).show();

                }
                else if(commentaire.getText().toString().matches("")){
                    Toast.makeText(getActivity(),"Vous n'avez pas entré de commentaire",Toast.LENGTH_LONG).show();

                }

                else{
                    BackgroudAddTask bw = new BackgroudAddTask(getContext());
                    String result = "";
                    try {
                        result = bw.execute("ajout_taches",text_nom_tache.getText().toString(),txtdeb.getText().toString(),txtfin.getText().toString(),commentaire.getText().toString()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(result.equals("1")){
                        Toast.makeText(getActivity(),"Tout est bon",Toast.LENGTH_LONG).show();//Entrez des Valeurs dans la base de donnés
                    }
                    else {
                        Toast.makeText(getActivity(),"Ya un Blem Fréroo",Toast.LENGTH_LONG).show();//Entrez des Valeurs dans la base de donnés
                    }


                }

            }
        });



        return v;
    }


}

