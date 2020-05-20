package com.example.myapplication;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class subActivity3 extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);

        webView=(WebView)findViewById(R.id.wv_stream);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        String url="http://192.168.0.12:8080/stream_simple.html";
        webView.loadUrl(url);
    }
}
