package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class quiz_3 extends AppCompatActivity {

    Button rightBt,wrongBt,nextbt;
    ImageView iv;
    TextView tv,nexttx,infom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz3);
        rightBt = (Button) findViewById(R.id.right3);
        wrongBt = (Button) findViewById(R.id.wrong3);
        iv= (ImageView) findViewById(R.id.answer3);
        tv=(TextView)findViewById(R.id.desc3);

        infom=(TextView)findViewById(R.id.info3);
        nextbt=(Button)findViewById(R.id.nextBtn3);
        rightBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iv.setImageResource(R.drawable.rightim);
                tv.setText("정답입니다!");

                nextbt.setBackgroundResource(R.drawable.nextimg);
                infom.setBackgroundResource(R.drawable.text_border);
                infom.setText("창밖으로 연기가 보이지 않으면 자신이 있는 것보다 위에서 불이 난 경우이다. 이럴때는 아래로 대피해야한다.");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_4.class);
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
                infom.setBackgroundResource(R.drawable.text_border);
                infom.setText("연기가 보이지 않으면 위에서 불이 난 경우이다.");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_3.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }

        });
    }

}


