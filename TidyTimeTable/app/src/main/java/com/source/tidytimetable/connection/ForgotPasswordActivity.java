package com.source.tidytimetable.connection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.source.tidytimetable.R;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.input_emailforgot)
    EditText emailForgotText;
    @BindView(R.id.btn_forgot)
    Button forgotButton;

    public static Activity log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ButterKnife.bind(this);

        log = this;

        forgotButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                reset();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void reset() {

        if(!validate()) {
            return;
        }

        forgotButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Envoie du mail...");
        progressDialog.show();

        String email = emailForgotText.getText().toString();
        String result = "";

        BackgroundForgeter bw = new BackgroundForgeter(this);
        try {
            result = bw.execute("sendmail",email).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result.equals("1")) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onForgotSuccess();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        } else {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onForgotFailed();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        }
    }

    public void onForgotSuccess() {
        forgotButton.setEnabled(true);
        Intent intent = new Intent(this, ResetActivity.class);
        startActivity(intent);
    }

    public void onForgotFailed() {
        Toast.makeText(getBaseContext(), "Aucun compte n'est associé à cet email", Toast.LENGTH_LONG).show();

        forgotButton.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        String email = emailForgotText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailForgotText.setError("Entrez une adresse email valide");
            valid = false;
        } else {
            emailForgotText.setError(null);
        }

        return valid;

    }

}
