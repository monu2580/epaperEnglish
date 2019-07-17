package com.deepesh.epaperenglish.Activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.deepesh.epaperenglish.Model.Util;
import com.deepesh.epaperenglish.R;

import java.util.Date;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    String url,title;
    // Our created menu to use
    private Menu mymenu;
    SwipeRefreshLayout swipeRefreshLayout;
    ViewTreeObserver.OnScrollChangedListener onScrollChangedListener;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        try {

            date=new Date();
            swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
            swipeRefreshLayout.setEnabled(false);
        /*swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(webView.getScrollY()==0){
                    webView.reload();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });*/



            Intent rcv=getIntent();
            url=rcv.getStringExtra(Util.URL_KEY);
            title=rcv.getStringExtra(Util.TITLE_KEY);

            //Toolbaar
            //Toolbar toolbar = (
            // Toolbar)findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);

            getSupportActionBar().setTitle(title);
            //toolbar.setSubtitle("Android-er.blogspot.com");
            //toolbar.setLogo(android.R.drawable.ic_menu_info_details);

        /*// add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/


            webView=(WebView)findViewById(R.id.webViewId);
            WebViewClient client=new WebViewClient();
            webView.setWebViewClient(client);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setUseWideViewPort(true);

            //must be declare in menifest file
            //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {


                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title+date);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                            Toast.LENGTH_LONG).show();
                }
            });

            if(url!=null){

                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);
            }else {

                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("https://www.google.co.in");
            }


        }catch (Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add our menu
        getMenuInflater().inflate(R.menu.menu1, menu);

        // We should save our menu so we can use it to reset our updater.
        mymenu = menu;

        //
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { int id=item.getItemId();
        switch (id){
            case R.id.refreshIcon:
                webView.reload();
                Toast.makeText(this, "Reloading..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.goback:
                webView.goBack();
                Toast.makeText(this, "<- Backward", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gonext:
                webView.goForward();
                Toast.makeText(this, "Forward ->", Toast.LENGTH_SHORT).show();
                break;
            case R.id.closeIcon:
                finish();
                Toast.makeText(this, "Close", Toast.LENGTH_SHORT).show();
                break;

        }
        /*if(item.getItemId()==android.R.id.home){
            //onBackPressed();
            finish();
        }*/
        return super.onOptionsItemSelected(item);
    }
}
