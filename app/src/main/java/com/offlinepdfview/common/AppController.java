package com.offlinepdfview.common;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;

/**
 * Created by sit78 on 02-11-2017.
 */

public class AppController extends Application {

    private static Context mContext;
    private Fetch fetch;

    @Override
    public void onCreate() {
        super.onCreate();
        AppController.mContext = getApplicationContext();


        //Set settings for Fetch
        new Fetch.Settings(this)
                .setAllowedNetwork(Fetch.NETWORK_ALL)
                .enableLogging(true)
                .setConcurrentDownloadsLimit(1)
                .apply();

        fetch = Fetch.getInstance(this);

        fetch.addFetchListener(new FetchListener() {
            @Override
            public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {

                Log.d("fetchDebug", "id:" + id + ",status:" + status + ",progress:" + progress
                        + ",error:" + error);

                Log.i("fetchDebug", "id: " + id + " downloadedBytes: " + downloadedBytes + " / fileSize: " + fileSize);
            }
        });
    }

    public static Context getAppContext() {
        return AppController.mContext;
    }

    public Fetch getFetch() {
        return fetch;
    }
}
