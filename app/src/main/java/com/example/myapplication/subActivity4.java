package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class subActivity4 extends AppCompatActivity {

    Button rightBt,wrongBt,nextbt;
    ImageView iv;
    TextView tv,nexttx,infom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub4);
        rightBt = (Button) findViewById(R.id.right1);
        wrongBt = (Button) findViewById(R.id.wrong1);
        iv= (ImageView) findViewById(R.id.answer1);
        tv=(TextView)findViewById(R.id.desc);

        infom=(TextView)findViewById(R.id.info);
        nextbt=(Button)findViewById(R.id.nextBtn);
        rightBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iv.setImageResource(R.drawable.rightimg);
                tv.setText("정답입니다!");

                nextbt.setBackgroundResource(R.drawable.nextimg);
                infom.setText("           다음문제");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), subActivity5.class);
                        startActivity(intent);
                    }
                });
            }
        });
        wrongBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //iv.setImageResource(R.drawable.wrongimg);
                iv.setImageResource(R.drawable.light);
                tv.setText("오답입니다!");

                nextbt.setBackgroundResource(R.drawable.retryimg);
                infom.setText("화재가 발생하면, 일단 주위에 알려야합니다.");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), subActivity4.class);
                        startActivity(intent);
                    }
                });
            }

        });
    }

}


