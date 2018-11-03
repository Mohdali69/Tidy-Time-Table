package com.source.tidytimetable.connection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.source.tidytimetable.main.MainActivity;
import com.source.tidytimetable.R;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.input_lastname)
    EditText lastNameText;
    @BindView(R.id.input_name)
    EditText nameText;
    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.input_confirmPassword)
    EditText confirmPasswordText;
    @BindView(R.id.btn_signup)
    Button signupButton;
    @BindView(R.id.link_login)
    TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void signup() {

        if (!validate()) {
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Création du compte...");
        progressDialog.show();

        String lastName = lastNameText.getText().toString();
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String result = "";

        BackgroundRegister bw = new BackgroundRegister(this);
        try {
            result = bw.execute("signup",lastName,name,email,password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result.equals("1")) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onSignupSuccess();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        } else {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        }

    }

    public void onSignupSuccess() {
        LoginActivity.email = emailText.getText().toString();
        String result = "";
        try {
            result = new BackgroundInfo(this).execute("info",LoginActivity.email,"").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        LoginActivity.id = result.substring(0, result.indexOf("/"));
        LoginActivity.name = nameText.getText().toString();
        LoginActivity.lastName = lastNameText.getText().toString();
        MainActivity.setUserStatut(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        signupButton.setEnabled(true);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Un compte email est déjà associé à ce compte", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String lastName = lastNameText.getText().toString();
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = confirmPasswordText.getText().toString();

        if (lastName.isEmpty() || lastName.equals("")) {
            lastNameText.setError("Entrez un nom");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        if (name.isEmpty() || name.equals("")) {
            nameText.setError("Entrez un prénom");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Entrez une adresse email valide");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("Mot de passe comprit entre 4 et 10 caractères");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (!(reEnterPassword.equals(password))) {
            confirmPasswordText.setError("Mot de passe différent");
            valid = false;
        } else {
            confirmPasswordText.setError(null);
        }

        return valid;
    }

}