package com.example.luiz.teacherassistent.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionTest {
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) ContextParse.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return false;
        else
            return true;
    }
}
