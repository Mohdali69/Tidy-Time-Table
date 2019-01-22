package com.source.tidytimetable.main;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.source.tidytimetable.R;
import com.source.tidytimetable.connection.*;
import com.source.tidytimetable.fragment.*;
import android.Manifest;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.source.tidytimetable.notification.BackgroundBadge;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    public static Activity activity;
    public static long lastTimeTouch;
    private static int nbBadgeCount;
    private static boolean sessionTimeout;
    public static FloatingActionButton fab;
    private static BottomNavigationViewEx bottomNav;
    private static Badge badge;
    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionTimeout = false;
        activity = this;
        context = this;

        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.enableAnimation(false);
        bottomNav.enableShiftingMode(false);
        bottomNav.enableItemShiftingMode(false);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = new AddTaskFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddTaskFragment()).commit();

            }
        });


        String result = "";
        try {
            result = new BackgroundInfo(MainActivity.activity).execute("exist","",LoginActivity.id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(result.equals("1")) {
            new BackgroundPhoto()
                    .execute("https://iutdoua-web.univ-lyon1.fr/~p1705290/www/images/" + LoginActivity.id + ".png");
        } else {
            bitmap = BitmapFactory.decodeResource(null, R.drawable.profil);
        }



        FirebaseMessaging.getInstance().subscribeToTopic("food");
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

    public static void logout() {
        setUserStatut(false);
        LoginActivity.someoneLogin = false;
        activity.finish();
    }

    public static void sessionTimeout() {
        sessionTimeout = true;
        setUserStatut(false);
        activity.finish();
    }

    public static void setUserStatut(final boolean b) {

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
                        .url("https://iutdoua-web.univ-lyon1.fr/~p1705290/www/statut.php")
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

    public static void newNotification() {
        MainActivity.nbBadgeCount++;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Your logic here...

                // When you need to modify a UI element, do so on the UI thread.
                // 'getActivity()' is required as this is being ran from a Fragment.
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nbBadgeCount > 0) {
                            badge.hide(false);
                        }
                        badge = addBadgeAt(1,nbBadgeCount);
                    }
                });
            }
        }, 0, 3000);
    }

 /*   private void setBadgeCount() {
        String result = "";
        try {
            result = new BackgroundBadge().execute("count","count").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(!result.equals("0")) {
            badge = addBadgeAt(1,nbBadgeCount);
        }
    }
    */

    private static Badge addBadgeAt(int position, int number) {

        return new QBadgeView(context)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(bottomNav.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState) {
                            new BackgroundBadge().execute("reset","reset");
                            nbBadgeCount = 0;
                        }
                    }
                });
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
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
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
                            .url("https://iutdoua-web.univ-lyon1.fr/~p1705290/www/upload.php")
                            .post(request_body)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        } else {
                            new BackgroundPhoto()
                                    .execute("https://iutdoua-web.univ-lyon1.fr/~p1705290/www/images/" + LoginActivity.id + ".png");
                            ProfilFragment.userChoosedPhoto = true;
                        }
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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_chart:
                            if (nbBadgeCount > 0) {
                                badge.hide(true);
                                nbBadgeCount = 0;
                                new BackgroundBadge().execute("reset","reset");
                            }
                            selectedFragment = new ChartFragment();
                            break;
                        case R.id.nav_empty:
                            return false;
                        case R.id.nav_profil:
                            selectedFragment = new ProfilFragment();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                        case R.id.fab:
                            selectedFragment = new AddTaskFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
        public void Clicker(View view) {
            fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment selectedFragment = new AddTaskFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AddTaskFragment()).commit();

                }
            });
         }
}
