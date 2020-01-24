package com.day1assignment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.app.ProgressDialog.show;

public class SecondActivity extends AppCompatActivity {
    private WebView wv;
    public static String URLTAG = "url";
    private String url;
    private ProgressDialog progDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wv = findViewById(R.id.webview);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        url = bundle.getString(URLTAG);
        progDailog = show(this, "Loading","Please wait...", true);
        progDailog.setCancelable(false);


        WebSettings webSettings = wv.getSettings();
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);

        wv.setWebViewClient(new myWebClient());

        wv.loadUrl(url);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            progDailog.show();

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;

        }
        @Override
        public void onPageFinished(WebView view, final String url) {
            progDailog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
