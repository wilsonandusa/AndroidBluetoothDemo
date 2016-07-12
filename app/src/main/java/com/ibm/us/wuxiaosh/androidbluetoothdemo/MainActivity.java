package com.ibm.us.wuxiaosh.androidbluetoothdemo;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.util.Set;

import io.saeid.fabloading.LoadingView;

public class MainActivity extends AppCompatActivity {
    final String TAG = MainActivity.class.getName();
    String selection;
    String whichInt;
    String selectionAddress;
    private boolean isServiceBound;
    private AbstractGatewayService service;
    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, className.toString() + " service is bound");
            isServiceBound = true;
            service = ((AbstractGatewayService.AbstractGatewayServiceBinder) binder).getService();
            service.setContext(MainActivity.this);
            Log.d(TAG, "Starting live data");

            try{
                service.startService();

                Log.d(TAG , "Connected");


            } catch (IOException ioe){
                Log.e(TAG, "Failure Starting Live Data");
                doUnbindService();
            }

        }
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, " service is unbound");
            isServiceBound = false;
        }
    };




    private void doBindService(){
        if(!isServiceBound){
            Log.d(TAG, "Binding OBD Service..");

            Log.e(TAG,"start intent 1");
            Intent serviceIntent = new Intent(this, MyGatewayService.class);
            Log.e(TAG,"intent finished");
            bindService(serviceIntent,serviceConn, Context.BIND_AUTO_CREATE);
            Log.e(TAG,"bindService");
        }
    }

    private void doUnbindService(){
        if(isServiceBound){
            if (service.isRunning()){
                service.stopService();

                Log.d(TAG,"Ready");
            }
            Log.e(TAG, "Unbinding OBD Service...");
            unbindService(serviceConn);
            isServiceBound = false;
            Log.e(TAG, "Disconnected");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final StringBuilder btNameString = new StringBuilder();
        final StringBuilder btAddressString = new StringBuilder();
        final BluetoothAdapter mBtAdaper = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pariedDevices = mBtAdaper.getBondedDevices();
        if (pariedDevices.size() > 0) {
            for (BluetoothDevice device : pariedDevices) {
                btNameString.append(device.getName()).append(",");
                btAddressString.append(device.getAddress()).append(",");
            }
        }
        final SharedPreferences sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(Constants.SHARED_PREFERENCES_BLUETOOTH_DEVICES_KEY, btNameString.toString());
        editor.putString(Constants.SHARED_PREFERENCES_BLUETOOTH_ADDRESS_KEY, btAddressString.toString());
        editor.apply();
        editor.commit();


        Button btButton = (Button) findViewById(R.id.list_device);
        Button startButton = (Button) findViewById(R.id.start_button);
        Button endButton = (Button) findViewById(R.id.end_button);
//        ProgressWheel wheel = new ProgressWheel(getApplicationContext());
//        wheel.spin();
        //==============================Prepared Work Finished========================================================

        btButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtAdaper == null || !mBtAdaper.isEnabled()) {
                    Toast.makeText(getApplicationContext(),
                            "This device does not support Bluetooth or it is disabled.",
                            Toast.LENGTH_LONG).show();
                }
                Log.e(TAG,"Creating Dialog");
                final SharedPreferences sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                final String[] btNames = sharedPrefs.getString(Constants.SHARED_PREFERENCES_BLUETOOTH_DEVICES_KEY, "").split(",");
                final String[] btAddress = sharedPrefs.getString(Constants.SHARED_PREFERENCES_BLUETOOTH_ADDRESS_KEY, "").split(",");
                final String theChosenOne = sharedPrefs.getString(Constants.SHARED_PREFERENCES_BLUETOOTH_SELECTION_KEY, "");

                for (String btName :btNames){
                    Log.e(TAG,btName);
                }
                final int theChosenOneInt;
                if (theChosenOne == "") {
                    theChosenOneInt = -1;
                } else {
                    theChosenOneInt = Integer.parseInt(theChosenOne);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose the OBD Device").setSingleChoiceItems(btNames, theChosenOneInt, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG,btNames[which]);
                        Log.e(TAG,btAddress[which]);
                        Log.e(TAG,Integer.toString(which));
                        selection = btNames[which];
                        whichInt = Integer.toString(which);
                        selectionAddress = btAddress[which];
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selection != null) {
                            Toast.makeText(getApplicationContext(), "Bluetooth Selected: " + selection, Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString(Constants.SHARED_PREFERENCES_BLUETOOTH_SELECTION_KEY, whichInt);
                            editor.putString(Constants.SHARED_PREFERENCES_BLUETOOTH_SELECTION_ADDRESS_KEY, selectionAddress);
                            editor.apply();
                            editor.commit();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Starting live data..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {


//                        long futureTime = System.currentTimeMillis() + 3000;
//                        while (System.currentTimeMillis() < futureTime) {
//                            Log.d(TAG, "I am counting time...");
//                        }
                        Log.e(TAG, "Counter Finished ...");
                        doBindService();

                    }
                }).start();
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Stopping live data..");
                doUnbindService();
            }
        });


    }


}
