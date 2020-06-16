package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        tv=(TextView)findViewById(R.id.desc1);

        infom=(TextView)findViewById(R.id.info1);
        nextbt=(Button)findViewById(R.id.nextBtn1);
        rightBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iv.setImageResource(R.drawable.rightim);
                tv.setText("정답입니다!");

                nextbt.setBackgroundResource(R.drawable.nextimg);
                infom.setText("화재가 발생하면 신속히 주변에 알려 대피할 수 있도록 해야한다.");
                infom.setBackgroundResource(R.drawable.text_border);
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_2.class);
                        //       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
        wrongBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
<<<<<<< HEAD
                //iv.setImageResource(R.drawable.wrongimg);
                iv.setImageResource(R.drawable.light);
=======
                iv.setImageResource(R.drawable.wromgim);
>>>>>>> a4a66ff52963659d8f4c6aebbd33688748876781
                tv.setText("오답입니다!");

                nextbt.setBackgroundResource(R.drawable.retryimg);
                infom.setText("화재가 발생하면, 일단 주위에 알려야합니다.");
                infom.setBackgroundResource(R.drawable.text_border);
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                getApplicationContext(), subActivity4.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }

        });
    }

}


