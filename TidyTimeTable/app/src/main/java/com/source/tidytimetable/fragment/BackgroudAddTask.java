package com.source.tidytimetable.fragment;

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

public class BackgroudAddTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... parms) {
        String type = parms[0];
        String nom = parms[1];
        String date_debut = parms[2];
        String date_fin = parms[3];
        String commentaire = parms[4];
        String add_task_login = "https://iutdoua-web.univ-lyon1.fr/~p1705290/www/addtask.php";
        if(type.equals("login")) {
            try {
                URL url = new URL(add_task_login);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("nom","UTF-8") + "=" + URLEncoder.encode(nom,"UTF-8") + "&"
                        + URLEncoder.encode("date_debut","UTF-8") + "=" + URLEncoder.encode(date_debut,"UTF-8")+"&"
                        + URLEncoder.encode("date_fin","UTF-8") + "=" + URLEncoder.encode(date_fin,"UTF-8")+"&"
                        + URLEncoder.encode("commentaire","UTF-8") + "=" + URLEncoder.encode(commentaire,"UTF-8");
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
