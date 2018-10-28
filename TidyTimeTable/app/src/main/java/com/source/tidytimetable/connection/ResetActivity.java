package com.source.tidytimetable.connection;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.source.tidytimetable.R;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetActivity extends AppCompatActivity {

    @BindView(R.id.pinView)
    Pinview codePin;
    @BindView(R.id.timeRemaining)
    TextView timeRemaining;
    @BindView(R.id.input_newpassword2)
    EditText newPasswordText;
    @BindView(R.id.input_confirmPassword2)
    EditText confirmPasswordText;
    @BindView(R.id.btn_reset)
    Button resetButton;

    private CountDownTimer cdt;
    private long timeLeft = 120000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ButterKnife.bind(this);

        final BackgroundReseter bw = new BackgroundReseter(this);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Toast.makeText(getBaseContext(), "Le code de reinitialisation a expiré", Toast.LENGTH_LONG).show();
                        finish();
                        bw.execute("off","","");
                    }
                }, 120000);

        startTimer();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        codePin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {
                newPasswordText.setFocusableInTouchMode(true);
                newPasswordText.requestFocus();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void reset() {

        if (!validate()) {
            return;
        }

        resetButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ResetActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Reinitialisation du mot de passe...");
        progressDialog.show();

        String code = codePin.getValue();
        String password = newPasswordText.getText().toString();
        String result = "";

        BackgroundReseter bw = new BackgroundReseter(this);
        try {
            result = bw.execute("reset",code,password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result.equals("1")) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onResetSuccess();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        } else {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onResetFailed();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        }

    }


    public void onResetSuccess() {
        resetButton.setEnabled(true);
        finish();
        ForgotPasswordActivity.log.finish();
    }

    public void onResetFailed() {
        Toast.makeText(getBaseContext(), "Le code de reinitialisation est incorrect", Toast.LENGTH_LONG).show();

        resetButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String password = newPasswordText.getText().toString();
        String reEnterPassword = confirmPasswordText.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            newPasswordText.setError("Mot de passe comprit entre 4 et 10 caractères");
            valid = false;
        } else {
            newPasswordText.setError(null);
        }

        if (!(reEnterPassword.equals(password))) {
            confirmPasswordText.setError("Mot de passe différent");
            valid = false;
        } else {
            confirmPasswordText.setError(null);
        }

        return valid;
    }

    public void startTimer() {
        cdt =  new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void updateTimer() {
        int minutes = (int) timeLeft / 60000;
        int secondes = (int) timeLeft % 60000 / 1000;
        String timeLeftText = "Temps restant : ";
        if(minutes>0) {
            timeLeftText += minutes + "min";
        }
        if(secondes<10) {
            timeLeftText += "0";
        }
        timeLeftText += secondes + "sec";
        timeRemaining.setText(timeLeftText);
    }

}