package com.example.root12_31.ssid;

import android.app.Activity;
import android.os.Bundle;

class Test_wifiActivity extends Activity {
    /** Called when the activity is first created. */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiAdmin wifiAdmin = new WifiAdmin(this);
        wifiAdmin.openWifi();
        wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("XXX", "XXX", 3));
    }
}