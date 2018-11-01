package com.source.tidytimetable;

import com.source.tidytimetable.connection.*;
import android.Manifest;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    private static Activity activity;
    public static long lastTimeTouch;
    private static boolean sessionTimeout;

    public static TextView infoText;
    public static ImageView profilIV;
    public ImageView modifyIV;
    public ImageView logoutIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sessionTimeout = false;
        activity = this;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        profilIV = (ImageView) findViewById(R.id.iv_profil);
        modifyIV = (ImageView) findViewById(R.id.iv_modify);
        logoutIV = (ImageView) findViewById(R.id.iv_logout);
        infoText = (TextView) findViewById(R.id.tv_info);

        logoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        setUserInformation();

        profilSetAction();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        lastTimeTouch = prefs.getLong("lastTimeTouch", System.currentTimeMillis());
        startJobService();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("lastTimeTouch", lastTimeTouch);

        editor.apply();
    }

    @Override
    public void onPause() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        super.onPause();
    }

    @Override
    public void onRestart() {
        startJobService();
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(sessionTimeout) {
            Toast.makeText(MainActivity.this,"Votre session a expirée", Toast.LENGTH_LONG).show();
        }
    }

    public void startJobService() {
        ComponentName componentName = new ComponentName(this, BackgroundJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

    private void profilSetAction() {

        profilIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(10)
                        .start();
            }
        });

    }

    public void logout() {
        userStatut(false);
        LoginActivity.someoneLogin = false;
        activity.finish();
    }

    public static void sessionTimeout() {
        sessionTimeout = true;
        userStatut(false);
        activity.finish();
    }

    public static void userStatut(final boolean b) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("current_user",LoginActivity.id)
                        .addFormDataPart("statut",valueOf(b))
                        .build();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8888/statut.php")
                        .post(request_body)
                        .build();

                try {

                    Response response = client.newCall(request).execute();

                    if(!response.isSuccessful()){
                        throw new IOException("Error : "+response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }

    public void setUserInformation() {
        infoText.setText(LoginActivity.name + " " + LoginActivity.lastName);
        String result = "";
        try {
            result = new BackgroundInfo(this).execute("exist",LoginActivity.id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(result.equals("1")) {
            new BackgroundPhoto(profilIV)
                    .execute("http://10.0.2.2:8888/images/" + LoginActivity.id + ".png");
        } else {
            profilIV.setImageDrawable(getResources().getDrawable(R.drawable.profil));
        }
    }

    @Override
    public void onUserInteraction() {
        lastTimeTouch = System.currentTimeMillis();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            profilSetAction();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("current_user",LoginActivity.id)
                            .addFormDataPart("image",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:8888/upload.php")
                            .post(request_body)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        new BackgroundPhoto(profilIV)
                                .execute("http://10.0.2.2:8888/images/" + LoginActivity.id + ".png");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            t.start();

        }
    }

    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

}
