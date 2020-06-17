package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class quiz_5 extends AppCompatActivity {

    Button rightBt,wrongBt,wrongBt2,nextbt;
    ImageView iv;
    TextView tv,nexttx,infom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz5);
        rightBt = (Button) findViewById(R.id.right5);
        wrongBt2 = (Button) findViewById(R.id.wrong5_2);
        wrongBt = (Button) findViewById(R.id.wrong5);
        iv= (ImageView) findViewById(R.id.answer5);
        tv=(TextView)findViewById(R.id.desc5);

        infom=(TextView)findViewById(R.id.info5);
        nextbt=(Button)findViewById(R.id.nextBtn5);
        rightBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iv.setImageResource(R.drawable.rightim);
                tv.setText("정답입니다!");

                nextbt.setBackgroundResource(R.drawable.nextimg);
                infom.setText("유독가스는 공기보다 가벼워 위로 뜨기 떄문에 몸을 최대한 낮추어 지나가는게 안전하다.");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(quiz_5.this) // TestActivity 부분에는 현재 Activity의 이름 입력.
                                .setMessage("퀴즈를 모두 풀었습니다!")     // 제목 부분 (직접 작성)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                                    public void onClick(DialogInterface dialog, int which){
                                        Toast.makeText(getApplicationContext(), "확인 누름", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(

                                                getApplicationContext(), MainActivity.class);
                                        startActivity(intent);// 실행할 코드
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                                    public void onClick(DialogInterface dialog, int which){
                                        Toast.makeText(getApplicationContext(), "취소 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
                                    }
                                })
                                .show();

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
                infom.setText("연기를 직접 마시면서 뛰는것은 매우 위험하다. 달리다 보면 숨이 가빠져 유독가스를 더 깊이 마시게된다");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_5.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }

        });
        wrongBt2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iv.setImageResource(R.drawable.wromgim);
                tv.setText("오답입니다!");

                nextbt.setBackgroundResource(R.drawable.retryimg);
                infom.setText("바닥이 열때문에 뜨거워져 있다면 몸에 화상을 입을 수 있으니 조심해야한다.");
                nextbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                getApplicationContext(), quiz_5.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                    }
                });
            }

        });
    }

}


