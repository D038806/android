package com.example.fongchishong.ch14_mymap2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback{

    static final int MIN_TIME = 1000, MIN_DIST = 0;
    boolean isGPSEnabled, isNetworkEnabled;
    private GoogleMap map;
    LatLng currPoint;
    TextView txv;
    LatLng preLoc;
    LocationManager locmgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locmgr = (LocationManager)getSystemService(LOCATION_SERVICE);
        txv = (TextView)findViewById(R.id.txv);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mark:
                map.clear();
                map.addMarker(new MarkerOptions().position(map.getCameraPosition().target).title("HERE"));
                break;
            case R.id.satellite:
                item.setChecked(!item.isChecked());
                if(item.isChecked()){
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }else{
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.traffic:
                item.setChecked(!item.isChecked());
                map.setTrafficEnabled(item.isChecked());
                break;
            case R.id.currLocation:
                map.clear();
                map.animateCamera(CameraUpdateFactory.newLatLng(currPoint));
                map.addMarker(new MarkerOptions().position(currPoint).title("目前位置"));
                break;
            case R.id.setGPS:
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        enableLocationUpdates(true);
    }

    @Override
    protected void onPause(){
        super.onPause();
        enableLocationUpdates(false);
    }



    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            txv.setText(String.format("緯度:%.4f，經度:%.4f ( %s 定位 )",
                    location.getLatitude(),
                    location.getLongitude(),
                    location.getProvider()));
            map.clear();
            currPoint = new LatLng(location.getLatitude(),location.getLongitude());
            if(map != null){
                map.animateCamera(CameraUpdateFactory.newLatLng(currPoint));
                map.addMarker(new MarkerOptions().position(currPoint).title("目前位置"));
                setPolyLine(location);
                preLoc = new LatLng(location.getLatitude(),location.getLongitude());
            }
        }else{
            txv.setText("暫時無法取得定位資訊...");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void checkPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults){
        if(requestCode == 200){
            if (grantResults.length >= 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {     //使用者不允許權限
                Toast.makeText(this, "程式需要定位權限才能運作", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableLocationUpdates(boolean isTurnOn){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){  //使用者已經允許定位權限
            if(isTurnOn){
                //檢查GPS與網路訂位是否可用
                isGPSEnabled = locmgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locmgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(!isGPSEnabled && !isNetworkEnabled){
                    // 無提供者，顯示提示訊息
                    Toast.makeText(this, "請確認已開啟定位功能", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "取得定位資訊中...", Toast.LENGTH_SHORT).show();
                    if(isGPSEnabled) locmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER , MIN_TIME, MIN_DIST, this);
                    if(isNetworkEnabled) locmgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, this);
                }
            }else{
                locmgr.removeUpdates(this);
            }
        }
    }

    private void setPolyLine(Location location){
        if(preLoc == null){
            preLoc = new LatLng(location.getLatitude(),location.getLongitude());
        }else{
            map.addPolyline(new PolylineOptions().add(preLoc,currPoint).width(5).color(Color.BLUE));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.zoomTo(18));
    }
}
