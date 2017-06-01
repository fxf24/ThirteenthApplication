package com.example.qudqj_000.a2017_06_01;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class MainActivity extends AppCompatActivity {
    EditText et1;
    String SERVER_IP = "172.20.10.2";
    int SERVER_PORT = 200;
    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = (EditText)findViewById(R.id.etmsg);
    }

    public void onClick(View v){
        msg = et1.getText().toString();
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"다음");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
        return super.onOptionsItemSelected(item);
    }

    Handler hd = new Handler();
    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Socket aSocket = new Socket(SERVER_IP, SERVER_PORT);

                ObjectOutputStream outstream = new ObjectOutputStream(aSocket.getOutputStream());
                outstream.writeObject(msg);
                outstream.flush();


                ObjectInputStream instream = new ObjectInputStream(aSocket.getInputStream());
                final Object obj = instream.readObject();

                hd.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), (String)obj, Toast.LENGTH_SHORT).show();
                    }
                });
                aSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

}
