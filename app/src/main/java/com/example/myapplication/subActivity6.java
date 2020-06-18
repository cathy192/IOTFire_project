package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.kyleduo.switchbutton.SwitchButton;

public class subActivity6 extends AppCompatActivity {
    Button stop,start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub6);
        //   start=(Button)findViewById(R.id.startButton);
        //  stop=(Button)findViewById(R.id.stopButton);
        SwitchButton switchButton = (SwitchButton) findViewById(R.id.sb_use_listener);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService();
                } else {
                    stopService();
                }
            }
        });
/*
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });*/
    }
    public void startService() {
        Intent serviceIntent = new Intent(this, MyService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);
    }
}
