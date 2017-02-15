package com.example.duyenbui.qldv.object;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectDetector {
    private Context _context;

    public ConnectDetector(Context context) {
        this._context = context;
    }

    public  boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if(info != null && info.isConnected()) {
//                  return true;
//                }
//            }
        if ( connectivity.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED || connectivity.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING || connectivity.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connectivity.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return  true;
        }
        else if  (
                connectivity.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connectivity.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  )  {

            return false;
        }

            return false;
    }

}
