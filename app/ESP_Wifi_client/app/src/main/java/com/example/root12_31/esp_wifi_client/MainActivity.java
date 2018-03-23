package com.example.root12_31.esp_wifi_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;
import java.lang.Runnable;
import java.lang.Thread;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {
    Button start ,stop_value;
    TextView replace;
    EditText address_value,port_value;
    EditText form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        start = (Button)findViewById(R.id.button);
        replace = (TextView)findViewById(R.id.textView);
        address_value = (EditText) findViewById(R.id.address);
        port_value = (EditText) findViewById(R.id.port);
        form = (EditText) findViewById(R.id.form);
        stop_value= (Button)findViewById(R.id.stop);
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    Socket socket;
                    //                    int from_value =Integer.parseInt(form.getText().toString()) ;
                    public void run() {
                        super.run();
                        try {
                            socket = new Socket("172.20.10.3",8087);
//                            InputStream in = socket.getInputStream();
                            OutputStream out = socket.getOutputStream();
                            PrintWriter printWriter =  new PrintWriter(socket.getOutputStream());

                            //out.write(Move_control.form);
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this,Move_control.class);
                            startActivity(intent);
                            finish();

//                           BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                            replace.setText(receive.readLine());
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
}