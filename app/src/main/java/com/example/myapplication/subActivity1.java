package com.example.myapplication;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;

public class subActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);

        TextView dataTxt = findViewById(R.id.textView);
        getJsonData(dataTxt);
    }
    //Json데이터 가져옴
    public void getJsonData(TextView dataTxt){
        JsonConnection jsonConnection=new JsonConnection();
        JsonObjectRequest objectRequest = jsonConnection.fetchData(dataTxt);
        MySingleton.getInstance(subActivity1.this).addToRequestQueue(objectRequest);
    }
}
