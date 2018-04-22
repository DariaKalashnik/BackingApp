package com.example.kalas.backingapp;

import android.app.Application;

import com.example.kalas.backingapp.utils.ConnectivityReceiver;

/**
 * Created by kalas on 4/2/2018.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
