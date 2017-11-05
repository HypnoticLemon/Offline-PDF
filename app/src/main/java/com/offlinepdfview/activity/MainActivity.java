package com.offlinepdfview.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.offlinepdfview.adapter.MainAdapter;
import com.offlinepdfview.common.AppController;
import com.offlinepdfview.common.DownloadUtility;
import com.offlinepdfview.common.Utils;
import com.offlinepdfview.data.MainData;
import com.offlinepdfview.listener.OnRecyclerItemClick;
import com.offlinepdfview.sit60.fragment_21_09.R;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FetchListener {

    private String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private List<MainData> mainData;
    private Fetch fetch;
    private long downloadId = -1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;
    private static int intNotificationId = 8001;
    private View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rootView = findViewById(R.id.rootView);
        mainData = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter = new MainAdapter(this, mainData, 0, itemClick);
        recyclerView.setAdapter(mainAdapter);
        Log.e(TAG, "onCreate: " + Utils.getMaterialPath(this));
        fetch = Fetch.getInstance(this);
        getBooksList();
    }

    private void getBooksList() {

        MainData book = new MainData();
        book.setId(1);
        book.setAuthor("Reto Meier");
        book.setTitle("Android 4 Application Development");
        book.setImageUrl("http://media.148apps.com/screenshots/1078342701/us-ipad-1-all-doc-scanner-pro-scan-pdfimagesany-types-of-multipages-document-change-presentation-shape-size-and-share-or-save-it.jpeg");
        book.setFileName("1");
        book.setFilePath("https://www.tutorialspoint.com/android/android_tutorial.pdf");
        mainData.add(book);

        book = new MainData();
        book.setId(2);
        book.setAuthor("Itzik Ben-Gan");
        book.setTitle("Microsoft SQL Server 2012 T-SQL Fundamentals");
        book.setImageUrl("http://api.androidhive.info/images/realm/2.png");
        book.setFileName("2");
        book.setFilePath("https://www.tutorialspoint.com/android/android_tutorial.pdf");
        mainData.add(book);

        book = new MainData();
        book.setId(3);
        book.setAuthor("Magnus Lie Hetland");
        book.setTitle("Beginning Python: From Novice To Professional Paperback");
        book.setImageUrl("http://api.androidhive.info/images/realm/3.png");
        book.setFileName("3");
        book.setFilePath("https://www.tutorialspoint.com/android/android_tutorial.pdf");
        mainData.add(book);

        book = new MainData();
        book.setId(4);
        book.setAuthor("Chad Fowler");
        book.setTitle("The Passionate Programmer: Creating a Remarkable Career in Software Development");
        book.setImageUrl("http://api.androidhive.info/images/realm/4.png");
        book.setFileName("4");
        book.setFilePath("https://www.tutorialspoint.com/android/android_tutorial.pdf");
        mainData.add(book);

        book = new MainData();
        book.setId(5);
        book.setAuthor("Yashavant Kanetkar");
        book.setTitle("Written Test Questions In C Programming");
        book.setImageUrl("https://api.androidhive.info/images/realm/5.png");
        book.setFileName("5");
        book.setFilePath("https://www.tutorialspoint.com/android/android_tutorial.pdf");
        mainData.add(book);

        mainAdapter = new MainAdapter(this, mainData, mainData.size(), itemClick);
        recyclerView.setAdapter(mainAdapter);
    }

    OnRecyclerItemClick itemClick = new OnRecyclerItemClick() {
        @Override
        public void ItemClick(int position, String data, View view, int id) {
            //openOrDownload(mainData.get(position));
            Log.e(TAG, "ItemClick: ");
            clearAllDownloads(mainData.get(position));
        }
    };

    private void clearAllDownloads(MainData mainData) {
        fetch.removeAll();
        createRequest(mainData);
    }

    private void createRequest(MainData mainData) {

        enqueueDownload(mainData);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , STORAGE_PERMISSION_CODE);
        } else {

        }*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE || grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //enqueueDownload(mainData);

        } else {
            Snackbar.make(rootView, R.string.permission_not_enabled, Snackbar.LENGTH_LONG).show();
        }
    }

    private void enqueueDownload(MainData mainData) {

        String url = mainData.getFilePath();
        String filePath = Environment.getExternalStorageDirectory().getParent() + Utils.setMaterialPath(this) + mainData.getFileName();
        Log.e(TAG, "enqueueDownload:filePath: " + filePath);

        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Request request = new Request(url, filePath);

        downloadId = fetch.enqueue(request);
        Log.e(TAG, "enqueueDownload: " + downloadId);

        /*setTitleView(request.getFilePath());
        setProgressView(Fetch.STATUS_QUEUED, 0);*/

        mNotifyManager = (NotificationManager) AppController.getAppContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(AppController.getAppContext());

        mBuilder.setContentTitle("Download material")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(100, 0, true);

        mNotifyManager.notify(intNotificationId, mBuilder.build());
        Toast.makeText(AppController.getAppContext(), "Downloading...", Toast.LENGTH_SHORT).show();
    }

    private void openOrDownload(MainData data) {
        String Local_File_Path = Utils.getMaterialPath(this) + data.getFileName();

        File file = new File(Local_File_Path);
        if (file.exists()) {
            startActivity(new Intent(MainActivity.this, PDFViewActivity.class).putExtra("file_name", data.getFileName()));
        } else {
            String fileUrl = data.getFilePath();
            if (Utils.isOnline(this)) {
                DownloadUtility.downloadFile(fileUrl, Environment.getExternalStorageDirectory().getParent() + Utils.setMaterialPath(this) + data.getFileName()
                        , new DownloadUtility.OnDownloadFileListener() {
                            @Override
                            public void onSuccess(File file) {
                                Toast.makeText(AppController.getAppContext(), "Downloaded: " + file.getName(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(AppController.getAppContext(), "Download failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onFailure: " + e.getMessage());
                            }
                        });
            }
        }
    }

    @Override
    public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {
        Log.e(TAG, "onUpdate:" + status);

        if (id == downloadId) {

            if (status == Fetch.STATUS_ERROR) {

                mBuilder.setContentTitle("Download material")
                        .setContentText("Filed!")
                        .setSmallIcon(R.mipmap.ic_launcher);
                mNotifyManager.notify(intNotificationId, mBuilder.build());

                showDownloadErrorSnackBar(error);

            } else {

                setProgressView(status, progress);
            }
        }
    }

    private void showDownloadErrorSnackBar(int error) {

        Toast.makeText(AppController.getAppContext(), "error...", Toast.LENGTH_SHORT).show();

        final Snackbar snackbar = Snackbar.make(rootView, "Download Failed: ErrorCode: "
                + error, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetch.retry(downloadId);
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    private void setProgressView(int status, int progress) {

        mBuilder.setContentTitle("Download material")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(100, progress, true);

        mNotifyManager.notify(intNotificationId, mBuilder.build());


        switch (status) {

            case Fetch.STATUS_QUEUED: {
                Log.e(TAG, "setProgressView: " + R.string.queued);
                Toast.makeText(this, R.string.queued, Toast.LENGTH_SHORT).show();
                break;
            }
            case Fetch.STATUS_DOWNLOADING:
            case Fetch.STATUS_DONE: {

                if (progress == -1) {
                    Log.e(TAG, "setProgressView: " + R.string.downloading);
                    Toast.makeText(this, R.string.downloading, Toast.LENGTH_SHORT).show();
                } else {

                    String progressString = getResources()
                            .getString(R.string.percent_progress, progress);

                    Log.e(TAG, "setProgressView: " + progressString);
                }
                break;
            }
            default: {
                Log.e(TAG, "setProgressView: " + R.string.status_unknown);
                break;
            }
        }
    }
}
