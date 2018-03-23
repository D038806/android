package com.example.root12_31.gps_app;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
public class home extends AppCompatActivity {
    Button GoMaps;
    final int PAGE_house = 1;
    final int PAGE_GoogleMap = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GoMaps = (Button)findViewById(R.id.button2);
        GoMaps.setOnClickListener(Goto);
    }
    public OnClickListener Goto = new OnClickListener(){
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_google_maps_api);
        }
    };
}
