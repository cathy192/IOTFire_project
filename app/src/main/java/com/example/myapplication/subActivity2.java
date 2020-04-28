package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

public class subActivity2 extends AppCompatActivity {
    public static Context mcontext;
    static int a=30;//센서1, 추후 업데이트
    public static String stateFunction(){//센서 값과 기준값 비교

        String state="0";// 현재 센서 값에 따른 현재 상황
        if(a<=20){
            state="green";
        }
        else if(a>20 && a<=40){
            state="yellow";
        }
        else if(a>40){
            state="red";
        }
        return state;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);
        mcontext=this;
    }





}
