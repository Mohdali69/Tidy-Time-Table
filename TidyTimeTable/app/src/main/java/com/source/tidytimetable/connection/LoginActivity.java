package com.source.tidytimetable.connection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.source.tidytimetable.main.BackgroundLog;
import com.source.tidytimetable.main.MainActivity;
import com.source.tidytimetable.R;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.link_forgotpassword)
    TextView forgotPasswordLink;
    @BindView(R.id.link_signup)
    TextView signupLink;

    public static String id;
    public static String lastName;
    public static String name;
    public static String email;

    public static boolean someoneLogin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                login();
            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        someoneLogin = someoneLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (someoneLogin) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void login() {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String result = "";

        if (!validate()) {
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authentification...");
        progressDialog.show();

        BackgroundLogger bw = new BackgroundLogger(this);
        try {
            result = bw.execute("login",email,password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result.equals("1")) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onLoginSuccess();
                            resetSessionTimer();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        } else {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        }

    }

    public void onLoginSuccess() {
        email = emailText.getText().toString();
        String result = "";
        try {
            result = new BackgroundInfo(this).execute("info",email,"").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        id = result.substring(0, result.indexOf("/"));
        lastName = result.substring(result.indexOf("/") + 1, result.indexOf("~"));
        name = result.substring(result.indexOf("~") + 1, result.length());
        MainActivity.setUserStatut(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        loginButton.setEnabled(true);
        emailText.getText().clear();
        emailText.clearFocus();
        passwordText.getText().clear();
        passwordText.clearFocus();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Email ou mot de passe incorrect", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

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

        return valid;
    }

    public boolean someoneLogin() {
        String result = "";
        try {
            result = new BackgroundLog(this).execute("online").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(!result.equals("-1")) {
            id = result.substring(0, result.indexOf("/"));
            lastName = result.substring(result.indexOf("/") + 1, result.indexOf("~"));
            name = result.substring(result.indexOf("~") + 1, result.length());
            return true;
        } else {
            return false;
        }
    }

    private void resetSessionTimer() {

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("lastTimeTouch", System.currentTimeMillis());

        editor.apply();
    }

}
