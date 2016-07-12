package com.ibm.us.wuxiaosh.androidbluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by wuxiaosh on 7/11/2016.
 */
public class BluetoothManager {

    private static final String TAG = BluetoothManager.class.getName();
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static BluetoothSocket connect(BluetoothDevice dev) throws IOException{

        BluetoothSocket sock = null;
        BluetoothSocket socketFallback = null;
        Log.d(TAG,"Start Bluetooth Connection...");
        try
        {
            sock = dev.createRfcommSocketToServiceRecord(myUUID);
            Log.d(TAG, "Probably gonna wait here...");
            sock.connect();
        }catch (Exception e1){
            Log.e(TAG, "There was an error while establishing Bluetooth connection, Failing back...", e1);

        }
        return sock;
    }

}
