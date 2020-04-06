package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;

    TextView time;
    final Handler timeHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.InitializeView();
        this.SetListener();

    }

    public void InitializeView() {
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        btn6 = (Button) findViewById(R.id.button6);

      //  final TextView time = (TextView) findViewById(R.id.DayTimer);
    }
        long now = System.currentTimeMillis();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date mdate = new Date(now);
        String mdataformate = simpleDate.format(mdate);
        //time.setText(mdataformate);



        protected void update(){
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    Log.d("timeTimerTask", "Time : " + mdataformate);
                    time.setText(mdataformate);
                }
            };

            timeHandler.post(updater);
        }






    public void SetListener() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "버튼 테스트", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(
                        getApplicationContext(), subActivity1.class);
                Log.v("테스트", "버튼");


                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("테스트", "버튼");
                Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent2= new Intent(getApplicationContext(), subActivity2.class);
                startActivity(intent2);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("테스트", "버튼");
                Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent3= new Intent(getApplicationContext(), subActivity3.class);
                startActivity(intent3);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent4= new Intent(getApplicationContext(), subActivity4.class);
                startActivity(intent4);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("테스트", "버튼");
                Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent5= new Intent(getApplicationContext(), subActivity5.class);
                startActivity(intent5);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("테스트", "버튼");
                Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent6= new Intent(getApplicationContext(), subActivity6.class);
                startActivity(intent6);
            }
        });

}
}
