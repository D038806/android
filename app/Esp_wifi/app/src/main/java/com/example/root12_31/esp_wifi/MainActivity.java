package com.example.root12_31.esp_wifi;

import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends Activity {

    TextView textResponse;
//    public static TextView states;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear, buttonDiscon, buttonSendMsg;
    EditText welcomeMsg;
    Socket socket;
    boolean socketStatus = false;
    public MyClientTask myClientTask;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        states = (TextView) findViewById(R.id.states);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonClear = (Button) findViewById(R.id.clear);
        buttonDiscon = (Button) findViewById(R.id.closeSocket);
        buttonSendMsg = (Button) findViewById(R.id.sendMsg);
        textResponse = (TextView) findViewById(R.id.response);
        welcomeMsg = (EditText) findViewById(R.id.welcomemsg);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonDiscon.setOnClickListener(buttonDisconnectOnCLickListener);



        buttonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textResponse.setText("");
                editTextAddress.setText("");
                editTextPort.setText("");
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    OnClickListener buttonConnectOnClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            if (socketStatus)
                Toast.makeText(MainActivity.this, "Already talking to a Socket!! Disconnect and try again!", Toast.LENGTH_LONG).show();
            else {
                socket = null;
//                String address = editTextAddress.getText().toString();
//                int port = Integer.parseInt(editTextPort.getText().toString());
                String tMsg = welcomeMsg.getText().toString();

//                if (address == null || port > 9999 || port < 1)
//                    Toast.makeText(MainActivity.this, "Please enter valid address/port", Toast.LENGTH_LONG).show();
//
////                else {
//                    try {
//                        myClientTask = new MyClientTask("172.20.10.3", 8087,tMsg);
//                        myClientTask.execute();
//
//                    }catch (Exception e){
//                        e.getMessage();
////                    }
//
//
//
//                }
            }
        }
    };

    OnClickListener buttonDisconnectOnCLickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!socketStatus)
                Toast.makeText(MainActivity.this, "SOCKET Already Closed!!", Toast.LENGTH_SHORT).show();
            else {
                try {
                    onDisconnect();
                    if (myClientTask.isCancelled()) {
                        socket.close();
                        Toast.makeText(MainActivity.this, "Socket Closed!", Toast.LENGTH_SHORT).show();
                        socketStatus = false;
                    } else {
                        Toast.makeText(MainActivity.this, "Couldn't Disconnect! Pls try again!", Toast.LENGTH_SHORT).show();
                        socketStatus = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }
    };

    OnClickListener sendMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = welcomeMsg.toString();
            if(msg.equals(""))
            {
                Toast.makeText(MainActivity.this, "Message is empty!!!", Toast.LENGTH_SHORT).show();
            }
            else if(!socketStatus)
            {
                Toast.makeText(MainActivity.this, "Please Establish Socket Connection first!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                MyClientTask myClientTask = new MyClientTask(editTextAddress
                    .getText().toString(), Integer.parseInt(editTextPort
                    .getText().toString()),
                    msg);
            myClientTask.execute();

            }

        }
    };

    public void onDisconnect() {
        myClientTask.cancel(true);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder().setName("Main Page").setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]")).build();
        return new Action.Builder(Action.TYPE_VIEW).setObject(object).setActionStatus(Action.STATUS_TYPE_COMPLETED).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class MyClientTask extends AsyncTask<Void, String, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
            Log.w("MSG", "Entering async task");
        }


        @Override
        protected Void doInBackground(Void... arg0) {


            //  DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                socketStatus = true;

                // dataOutputStream = new DataOutputStream(socket.getOutputStream());

//                if(msgToServer != null){
//                    dataOutputStream.writeUTF(msgToServer);
//                }
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
                socketStatus = false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }

            Log.w("MSG", "Inside while loop for retrieving data");
            while (!isCancelled()) {
                try {
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    response = dataInputStream.readUTF();

                    if (!response.isEmpty()) {
                        publishProgress(response);
                        Log.w("Data:", response);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


//                if (dataOutputStream != null) {
//                    try {
//                        dataOutputStream.close();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            try {
                Log.w("MSG", "Stopping async task");
                socket.close();
                socketStatus = false;
            } catch (IOException e) {
                e.printStackTrace();
                socketStatus = true;
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textResponse.setText(values[0]);
            Toast.makeText(MainActivity.this, "Server:" + values[0], Toast.LENGTH_LONG).show();
            Log.w("MSG", "Updating with msg");
        }


        @Override
        protected void onPostExecute(Void result) {
            Log.w("MSG", "On postExecute method..");
            textResponse.setText(response);
            super.onPostExecute(result);
        }

    }

}