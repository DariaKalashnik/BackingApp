package com.example.kalas.backingapp.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.kalas.backingapp.MyApplication;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = false;
        try {
           isConnected = isOnline();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isOnline);
    }
}

