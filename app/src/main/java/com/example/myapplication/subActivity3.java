package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class subActivity3 extends AppCompatActivity {
    WebView webView;
    EditText editText;
    String url="http://192.168.0.12:8080/stream_simple.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);
        Button bt_save=(Button)findViewById(R.id.Savebutton);
        editText= (EditText)findViewById(R.id.videoUrl);
        webView=(WebView)findViewById(R.id.wv_stream);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        webView.loadUrl(url);
        getPreferences();
        bt_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                savePreferences(editText.getText().toString());
            }

        });
    }
    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String sUrl = "http://192.168.0.12:8080/stream_simple.mjpeg";
                //pref.getString("stream", (String) url);//디폴터값이 왜 안되???
        editText.setText(sUrl);
    }

    //save
    private void savePreferences(String s){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("stream", s);
        editor.commit();
        finish();
    }

}
