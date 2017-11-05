package com.offlinepdfview.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.offlinepdfview.common.Utils;
import com.offlinepdfview.sit60.fragment_21_09.R;

import java.io.File;

/**
 * Created by sit78 on 31-10-2017.
 */

public class PDFViewActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener, OnPageScrollListener, OnErrorListener, OnRenderListener, OnTapListener {

    private PDFView pdfView;
    private String TAG = "PDFViewActivity";
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        String Local_File_Path = Utils.getMaterialPath(this) + getIntent().getStringExtra("file_name");

        webView = (WebView) findViewById(R.id.webView);
        webView.setVisibility(View.GONE);
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.getSettings().setJavaScriptEnabled(true);

        File file = new File(Local_File_Path);

        //webView.loadUrl("https://docs.google.com/file" + s);
        //  webView.loadUrl("https://docs.google.com/viewer?url=" + file);
        webView.loadUrl("https://docs.google.com/viewer?url=http://research.google.com/archive/bigtable-osdi06.pdf");
        pdfView = (PDFView) findViewById(R.id.pdfView);


        //File file = new File(s);

        if (file.exists()) {
            pdfView.fromFile(file)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .onLoad(this)
                    .defaultPage(0)
                    .onPageChange(this)
                    .onPageScroll(this)
                    .onError(this)
                    .onRender(this) // called after document is rendered for the first time
                    // called on single tap, return true if handled, false to toggle scroll handle visibility
                    .onTap(this)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(new DefaultScrollHandle(this, true))
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(10)
                    .load();
        }


    }


    @Override
    public void loadComplete(int nbPages) {
        Log.e(TAG, "loadComplete: " + nbPages);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Log.e(TAG, "onPageChanged: page:" + page + "| pageCount:" + pageCount);
    }

    @Override
    public void onPageScrolled(int page, float positionOffset) {

    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, "onError: " + t.getMessage());
        t.printStackTrace();
    }

    @Override
    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
        Log.e(TAG, "onInitiallyRendered: " + nbPages);
    }

    @Override
    public boolean onTap(MotionEvent e) {

        return false;
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
