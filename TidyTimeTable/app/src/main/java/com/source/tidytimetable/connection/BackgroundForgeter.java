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

public class BackgroundForgeter extends AsyncTask<String,Void,String> {

    Context context;

    public BackgroundForgeter(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... parms) {
        String type = parms[0];
        String email = parms[1];
        String forgot_url = "http://10.0.2.2:8888/sendmail.php";

        if(type.equals("sendmail")) {
            try {
                URL url = new URL(forgot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8") + "=" + URLEncoder.encode(email,"UTF-8");
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
        }
        return null;
    }

}
