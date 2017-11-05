package com.offlinepdfview.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;

/**
 * Created by sit78 on 02-11-2017.
 */

public class Utils {

    public static String setMaterialPath(Context c) {
        File dir = new File(getMaterialPath(c));
        if (dir.isDirectory() || !dir.exists()) {
            dir.mkdirs();
        }
        return "/.offlinepdfview/";
    }

    public static String getMaterialPath(Context c) {
        return Environment.getExternalStorageDirectory() + "/.offlinepdfview/";
    }

    public static String removeWhiteSpaceFromURL(String url) {
        return url.replaceAll(" ", "%20");
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressLint("MissingPermission") NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
