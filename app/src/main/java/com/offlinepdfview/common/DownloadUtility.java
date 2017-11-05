package com.offlinepdfview.common;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.offlinepdfview.sit60.fragment_21_09.R;

import java.io.File;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by sit78 on 02-11-2017.
 */

public class DownloadUtility {

    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;
    private static int intNotificationId = 8001;

    public interface OnDownloadFileListener {
        public void onSuccess(File file);

        public void onFailure(Exception e);
    }

    public static void downloadFile(String url, String filePath, final OnDownloadFileListener mOnDownloadFileListener) {

        Log.e(TAG, "downloadFile:filePath =  " + filePath);

        mNotifyManager = (NotificationManager) AppController.getAppContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(AppController.getAppContext());

        mBuilder.setContentTitle("Download material")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(100, 0, true);

        mNotifyManager.notify(intNotificationId, mBuilder.build());
        Toast.makeText(AppController.getAppContext(), "Downloading...", Toast.LENGTH_SHORT).show();

      /*  Ion.with(AppController.getAppContext())
                .load(Utils.removeWhiteSpaceFromURL(url))
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        System.out.println("" + downloaded + " / " + total);
                    }
                })
                .write(new File(filePath))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (e == null && file.exists()) {
                            mBuilder.setContentText("Download completed")
                                    .setProgress(0, 0, false);
                            mNotifyManager.notify(intNotificationId, mBuilder.build());
                            mOnDownloadFileListener.onSuccess(file);
                        } else {
                            mBuilder.setContentText("Download failed")
                                    .setProgress(0, 0, false);
                            mNotifyManager.notify(intNotificationId, mBuilder.build());

                        }
                    }
                });*/
    }

    //mOnDownloadFileListener.onSuccess(file);
    //mOnDownloadFileListener.onFailure(e);
}
