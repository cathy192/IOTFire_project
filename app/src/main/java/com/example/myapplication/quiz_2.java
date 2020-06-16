package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class quiz_2 extends AppCompatActivity {

    Button rightBt,wrongBt,nextbt;
    ImageView iv,talk;
    TextView tv,nexttx,infom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz2);
        rightBt = (Button) findViewById(R.id.right2);
        wrongBt = (Button) findViewById(R.id.wrong2);
        iv= (ImageView) findViewById(R.id.answer2);
        tv=(TextView)findViewById(R.id.desc2);

        infom=(TextView)findViewById(R.id.info2);
        nextbt=(Button)findViewById(R.id.nextBtn2);
        rightBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iv.setImageResource(R.drawable.rightim);
                tv.setText("정답입니다!");

                nextbt.setBackgroundResource(R.drawable.nextimg);
                infom.setBackgroundResource(R.drawable.text_border);
                infom.setText("불이 크게 나기 전이라면 큰 냄비뚜껑으로 불을 끌 수 있다. 밀가루나 베이킹 파우더를 뿌려도 좋다");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_3.class);
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
                infom.setText("기름이 있는 프라이펜에 물을 부으면 기름이 사방으로 튀면서 불이 더 커진다.");
                infom.setBackgroundResource(R.drawable.text_border);                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }

        });
    }

}


