package com.ibm.us.wuxiaosh.androidbluetoothdemo;

import android.bluetooth.*;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;

/**
 * Created by wuxiaosh on 7/11/2016.
 */
public class MyGatewayService extends AbstractGatewayService{
    private static final String TAG = MyGatewayService.class.getName();
    private BluetoothDevice dev = null;
    private BluetoothSocket sock = null;


    @Override
    public void startService() throws IOException {
        final String remoteDevice = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Constants.SHARED_PREFERENCES_BLUETOOTH_SELECTION_ADDRESS_KEY, "");

        if (remoteDevice == null||"".equals(remoteDevice)){
            Toast.makeText(getApplicationContext(),"No Bluetooth device selected...",Toast.LENGTH_SHORT).show();
            Log.e(TAG,"No Bluetooth device selected...");
            stopService();
            throw new IOException();
        }else{
            final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            dev = btAdapter.getRemoteDevice(remoteDevice);
            Log.d(TAG,"Stop bluetooth discovery...");
            btAdapter.cancelDiscovery();
            Log.d(TAG,"Start Service..");
            try{
                startServiceConnection();
            }catch (Exception e){
                Log.e(TAG, "There was an error while establishing connection..." + e.getMessage());
                stopService();
                throw new IOException();
            }
        }
    }

    private void startServiceConnection() throws IOException, InterruptedException {
        Log.d(TAG, "Start the connection");
        isRunning = true;

        try{
            sock = com.ibm.us.wuxiaosh.androidbluetoothdemo.BluetoothManager.connect(dev);
        }catch (Exception e2){
            Log.e(TAG, "There was an error while connecting... stop...");
            stopService();
            throw new IOException();
        }


    }

    @Override
    protected void executeQueue(){
        Log.d(TAG,"Executing...");
        while(!Thread.currentThread().isInterrupted()){
            //Log.d(TAG,"Executing ....................");
        }
    }
    @Override
    public void stopService() {
        isRunning = false;

        if (sock!=null){
            try{
                sock.close();
            }catch (IOException e){
                Log.e(TAG, e.getMessage());
            }
            stopSelf();
        }
    }
}
