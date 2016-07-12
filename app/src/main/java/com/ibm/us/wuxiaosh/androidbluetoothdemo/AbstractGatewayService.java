package com.ibm.us.wuxiaosh.androidbluetoothdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public abstract class AbstractGatewayService extends Service {
    private static final String TAG = AbstractGatewayService.class.getName();
    private final IBinder binder = new AbstractGatewayServiceBinder();
    protected Context ctx;
    protected boolean isRunning = false;

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "Thread Run...");
                long futureTime = System.currentTimeMillis()+10000;
                while(System.currentTimeMillis()<futureTime) {
                    executeQueue();
                }
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Creating Service...");
        t.start();
        Log.d(TAG, "Service Creating...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Destroying service");
        t.interrupt();
        Log.d(TAG,"Service Destroyed");
    }

    class AbstractGatewayServiceBinder extends Binder {
        public AbstractGatewayService getService(){
            return AbstractGatewayService.this;
        }
    }
    public boolean isRunning() {
        return isRunning;
    }

    public void setContext(Context c) {
        ctx = c;
    }

    abstract protected void executeQueue();

    abstract public void startService() throws IOException;

    abstract public void stopService();
}
