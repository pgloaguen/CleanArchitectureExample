package com.pgloaguen.data.net.utils;

import android.Manifest;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

import javax.inject.Inject;

public class ConnectionUtils {

    private ConnectivityManager connectivityManager;

    @Inject
    public ConnectionUtils(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    /**
     * Shows whether you are connected.
     *
     * @return true if it is connected to a network, false otherwise
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected());
    }

    /**
     * Return a name describe the type of the network
     *
     * @return name of the network type "WIFI" or "MOBILE" if is connected, null otherwise
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public String getConnectionType() {
        return isConnected() ? connectivityManager.getActiveNetworkInfo().getTypeName() : null;
    }
}
