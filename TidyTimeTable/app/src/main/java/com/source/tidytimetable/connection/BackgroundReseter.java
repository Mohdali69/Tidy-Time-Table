package com.source.tidytimetable.connection;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundReseter extends AsyncTask<String,Void,String> {

    Context context;

    public BackgroundReseter(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... parms) {
        String type = parms[0];
        String code = parms[1];
        String password = parms[2];
        String reset_url = "https://iutdoua-web.univ-lyon1.fr/~p1705290/www/reset.php";
        String off_url = "https://iutdoua-web.univ-lyon1.fr/~p1705290/www/off.php";

        if(type.equals("reset")) {
            try {
                URL url = new URL(reset_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("code","UTF-8") + "=" + URLEncoder.encode(code,"UTF-8") + "&"
                        + URLEncoder.encode("password","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8");
                bw.write(post_data);
                bw.flush();
                bw.close();
                outputStream.close();
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
        } else if (type.equals("off")) {
            try {
                URL url = new URL(off_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
