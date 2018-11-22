package com.source.tidytimetable.connection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.source.tidytimetable.main.MainActivity;

import java.io.InputStream;

public class BackgroundPhoto extends AsyncTask<String, Void, Bitmap> {

    protected Bitmap doInBackground(String... urls) {
        String photo_url = urls[0];
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(photo_url).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return icon;
    }

    protected void onPostExecute(Bitmap result) {
        MainActivity.bitmap = result;
    }
}