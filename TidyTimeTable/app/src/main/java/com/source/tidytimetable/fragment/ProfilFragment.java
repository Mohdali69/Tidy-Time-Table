package com.source.tidytimetable.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.source.tidytimetable.*;
import com.source.tidytimetable.connection.LoginActivity;
import com.source.tidytimetable.main.MainActivity;

public class ProfilFragment extends Fragment {

    private static TextView infoText;
    private static ImageView profilIV;
    public static boolean userChoosedPhoto;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_profil, container, false);
        userChoosedPhoto = false;

        profilIV = (ImageView) v.findViewById(R.id.iv_profil);
        infoText = (TextView) v.findViewById(R.id.tv_info);

        setUserInformation();
        profilSetAction();
        setHandler();

        return v;
    }

    private void setUserInformation() {
        infoText.setText(LoginActivity.name + " " + LoginActivity.lastName);
        profilIV.setImageBitmap(MainActivity.bitmap);
    }

    private void profilSetAction() {

        profilIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(MainActivity.activity)
                        .withRequestCode(10)
                        .start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(!userChoosedPhoto) {
                            Log.v("Debug","");
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });

    }

    private void setHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Mise à jour de la photo de profil...");

                progressDialog.show();new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                profilIV.setImageBitmap(MainActivity.bitmap);
                                userChoosedPhoto = false;
                                progressDialog.dismiss();
                            }
                        }, 1500);
            }
        };
    }

    private void refreshFragment() {
        onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
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

}
