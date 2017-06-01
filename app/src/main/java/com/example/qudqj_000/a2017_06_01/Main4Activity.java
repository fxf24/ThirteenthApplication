package com.example.qudqj_000.a2017_06_01;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main4Activity extends AppCompatActivity {
    EditText et1;
    TextView textView;
    String urlstr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        et1 = (EditText)findViewById(R.id.url);
        textView = (TextView)findViewById(R.id.text);
    }

    public void onClick(View v){
        urlstr = et1.getText().toString();
        thread.setDaemon(true);
        thread.start();
    }

    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                URL url = new URL(urlstr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final String data = readData(urlConnection.getInputStream());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(data);
                        }
                    });
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    String readData (InputStream is){
        String data = "";
        Scanner s = new Scanner(is);
        while (s.hasNext()) data += s.nextLine() + "\n";
        s.close();
        return data;
    }
}
