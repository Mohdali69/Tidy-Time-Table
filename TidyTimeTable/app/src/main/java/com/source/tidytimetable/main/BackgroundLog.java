package com.source.tidytimetable.main;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BackgroundLog extends AsyncTask<String,Void,String> {

    Context context;

    public BackgroundLog(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... parms) {
        String type = parms[0];
        String log_url = "https://iutdoua-web.univ-lyon1.fr/~p1705290/www/online.php";

        if (type.equals("online")) {
            try {
                URL url = new URL(log_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
