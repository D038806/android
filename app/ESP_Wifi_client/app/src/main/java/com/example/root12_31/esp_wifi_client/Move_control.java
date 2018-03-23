package com.example.root12_31.esp_wifi_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.OutputStream;
import java.net.Socket;


public class Move_control  extends MainActivity {
    Button btnforward , btnback ,btnleft ,btnright ,btnback_paper,btnstop;
    public static byte form=0;
    public Move_control(Socket socket) {
        super();
        setContentView(R.layout.moveview);
        btnforward = (Button) findViewById(R.id.forward);
        btnback = (Button) findViewById(R.id.back);
        btnleft = (Button) findViewById(R.id.left);
        btnstop = (Button) findViewById(R.id.stop);
        btnright = (Button) findViewById(R.id.right);
        btnback_paper = (Button) findViewById(R.id.back_paper);
            //initBtn

        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form=49;
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form=50;
            }
        });

        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form=51;
            }
        });

        btnright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form=52;
            }
        });

        btnback_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form=48;
            }
        });
        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form=53;
            }
        });
        btnback_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Move_control.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setform(byte form){
        this.form=form;
    }
    public byte getform(){
        return form;
    }
}
