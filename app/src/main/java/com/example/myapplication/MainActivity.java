package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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

import static com.example.myapplication.subActivity2.mcontext;
import static com.example.myapplication.subActivity2.stateFunction;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;

    TextView time;
    final Handler timeHandler = new Handler();

    NotificationManager manager; NotificationCompat.Builder builder; private static String CHANNEL_ID = "channel1"; private static String CHANEL_NAME = "Channel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.InitializeView();
        this.SetListener();
        if(
                ((subActivity2)mcontext).stateFunction()=="yellow")
        {
        this.showNoti();}
        //알람 울리기





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




    public void showNoti(){
        PendingIntent mpendingIntent = PendingIntent.getActivity(
                MainActivity.this,0,
                new Intent(getApplicationContext(),MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //버전 오레오 이상일 경우
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             manager.createNotificationChannel(
                     new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
             builder = new NotificationCompat.Builder(this, CHANNEL_ID); //하위 버전일 경우

         }else{
        builder = new NotificationCompat.Builder(this);
         } //알림창 제목
         builder.setContentTitle("IOT화재감지기"); //알림창 메시지
         builder.setContentText("현재 상태 위험"); //알림창 아이콘
        builder.setContentIntent(mpendingIntent);
      //  builder.setAutoCancel(true);//누르면 알림 삭제
         builder.setSmallIcon(R.drawable.check);
         Notification notification = builder.build(); //알림창 실행
         manager.notify(1,notification);
    }







//버튼 눌러 다른 페이지로 이동
    public void SetListener() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "버튼 테스트", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(
                        getApplicationContext(), subActivity1.class);


                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(getApplicationContext(), subActivity2.class);
                startActivity(intent2);



            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent5= new Intent(getApplicationContext(), subActivity5.class);
                startActivity(intent5);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent6= new Intent(getApplicationContext(), subActivity6.class);
                startActivity(intent6);
            }
        });

}
}
