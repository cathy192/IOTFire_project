package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class quiz_4 extends AppCompatActivity {

    Button rightBt,wrongBt,nextbt;
    ImageView iv;
    TextView tv,nexttx,infom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz4);
        rightBt = (Button) findViewById(R.id.right4);
        wrongBt = (Button) findViewById(R.id.wrong4);
        iv= (ImageView) findViewById(R.id.answer4);
        tv=(TextView)findViewById(R.id.desc4);

        infom=(TextView)findViewById(R.id.info4);
        nextbt=(Button)findViewById(R.id.nextBtn4);
        rightBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iv.setImageResource(R.drawable.rightim);
                tv.setText("정답입니다!");

                nextbt.setBackgroundResource(R.drawable.nextimg);
                infom.setText("불이 났을 때는 비상계단으로 탈출하는 것이 가장 안전한다. 하지만 비상계단에 연기가 가득 차 있으면 다른길로 가야한다.");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_5.class);
                        startActivity(intent);
                    }
                });
            }
        });
        wrongBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iv.setImageResource(R.drawable.wromgim);
                tv.setText("오답입니다!");

                nextbt.setBackgroundResource(R.drawable.retryimg);
                infom.setText("물이 있는 화장실은 피할 곳이 없을 때 구조를 잠시 기다리기에 괜찮으나 불이 옮겨 붙거나 연기가 차면 역시 위험하다.");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_4.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }

        });
    }

}


