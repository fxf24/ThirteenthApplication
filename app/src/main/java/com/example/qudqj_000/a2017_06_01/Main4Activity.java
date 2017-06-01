package com.example.qudqj_000.a2017_06_01;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Main4Activity extends AppCompatActivity {
    EditText et1, et2;
    TextView textView;
    String userid = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        et1 = (EditText)findViewById(R.id.userId);
        et2 = (EditText)findViewById(R.id.Password);
        textView = (TextView)findViewById(R.id.log_text);
    }

    public void onClick(View v){
        userid = et1.getText().toString();
        password = et2.getText().toString();
        if(userid.equals("") || password.equals("")){
            Toast.makeText(this, "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        thread.setDaemon(true);
        thread.start();
    }

    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://jerry1004.dothome.co.kr/info/login.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                String postData = "userid=" + URLEncoder.encode(userid)
                        + "&password=" + URLEncoder.encode(password);

                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                InputStream inputStream;

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    inputStream = urlConnection.getInputStream();
                else
                    inputStream = urlConnection.getInputStream();

                final String result = readData(inputStream);
                Log.d("data", result);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(result.equals("FAIL")){
                            textView.setText("로그인에 실패했습니다.");
                        }
                        else{
                            textView.setText(result + "님이 로그인에 성공하셨습니다.");
                        }
                    }
                });
                urlConnection.disconnect();
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
        while (s.hasNext()) data += s.nextLine();
        s.close();
        return data;
    }
}
