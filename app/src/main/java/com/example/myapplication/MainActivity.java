package com.example.myapplication;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private static final int MESSAGE_TEST_TYPE = 1004;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    String server_url =
            "https://api.thingspeak.com/channels/990298/fields/1.json?results=10";
    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    //json파일에서 화재 여부 받아오기
    Handler mHandler;   //Thred에서 사용하는 핸들러-> 혹시 몰라서 남겨둠
    TimerTask timerTask;
    AsyncHttpClient client;
    String channel="990298";
    //http://api.thingspeak.com/channels/990298/feed/last.json
    String url = "https://thingspeak.com/channels/"+channel+"/feeds.json?results=1";

    String state="기본";

    int fireRisk=0;
    Intent passedIntent = getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.InitializeView();
        this.SetListener();

        createTimer();
        Timer timer=new Timer();
        //5초에 한번씩 타이머가 울리도록 설정 ( 5초에 한번씩 서버에서 센서 정보를 받아옴 )
        timer.schedule(timerTask,0,5000);


    }
    void createTimer(){
        //타이머 객체 생성
        timerTask=new TimerTask() {
            @Override
            //타이머가 지정한 시간이 되면
            public void run() {
                //비동기 http통신 라이브러리를 사용하여 url에 접근한다. ( url="http://api.thingspeak.com/channels/"+채널번호+"/feed/last.json" )
                client.get(url, new JsonHttpResponseHandler() {
                    @Override
                    //Json 파일을 받아오는게 성공하면
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e("TAG","Http get Success");
                        try {
                            JSONObject jsonObject=response.getJSONArray("feeds").getJSONObject(0);
                            //JSON객체에서 사용할 값(센서 값)만 추출하여 저장한다.
                            String strTem=jsonObject.getString("field1");   //온도
                            String strCo = jsonObject.getString("field3");  //CO

                            //상태 변경
                            //온도>=80, CO(일산화탄소)의 농도>=0.05%,LPG>=0.05% 이면 화재가 났다고 판단한다.
                            if(Float.valueOf(strCo)>=50) {     // + LPG, 연기
                                if(Float.valueOf(strTem)>=80) {   // + 불꽃 세기
                                    showNoti("화재 발생", "화재가 발생하였습니다. 신속하게 대피하여주시기 바랍니다.");
                                    state="화재";
                                }
                                else{
                                    showNoti("주의", "가스가 기준치 있습니다. 화재에 주의해주세요");
                                    //가스의 농도 수치로 알람에 나타낼 수 있도록 하기
                                    state="주의";
                                }
                            }
                            else{
                                state="기본";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    //url에 접근이 실패하면 안드로이드 어플리케이션 화면에 에러가 일어났음을 알린다.
                    @Override
                    public boolean getUseSynchronousMode(){
                        return false;
                    }

                });
            }
        };

    }

    //알람 쓰레드(혹시 몰라서 남겨둠)==================================================
    public void testAlram(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message mMessage=mHandler.obtainMessage(MESSAGE_TEST_TYPE);
                mHandler.sendMessage(mMessage);
            }
        }).start();
    }
    //=========================================================================

    public void InitializeView() {
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        btn6 = (Button) findViewById(R.id.button6);
        //  final TextView time = (TextView) findViewById(R.id.DayTimer);

        //쓸모없는 쓰레드인데 혹시 나중에 쓸까봐 남겨둠=======================
        mHandler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what==MESSAGE_TEST_TYPE){
                    //showNoti();
                }
            }
        };
        //===================================================
        client = new AsyncHttpClient();
    }

    public void showNoti(String title, String text){
        PendingIntent mpendingIntent = PendingIntent.getActivity(
                MainActivity.this,0,
                new Intent(getApplicationContext(),MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        //하위 버전일 경우
        }else{
            builder = new NotificationCompat.Builder(this);
        }

        //알림창 제목
        builder.setContentTitle(title); //알림창 메시지
        builder.setContentText(text); //알림창 아이콘
        builder.setContentIntent(mpendingIntent);

        builder.setSmallIcon(R.drawable.check);
        Notification notification = builder.build(); //알림창 실행
        manager.notify(1,notification);
    }

    //버튼 눌러 다른 페이지로 이동
    public void SetListener() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getApplicationContext(), "버튼 테스트", Toast.LENGTH_LONG).show();
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

             //   Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent3= new Intent(getApplicationContext(), subActivity3.class);
                startActivity(intent3);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent4= new Intent(getApplicationContext(), subActivity4.class);
                startActivity(intent4);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent5= new Intent(getApplicationContext(), subActivity5.class);
                startActivity(intent5);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  Toast.makeText(getApplicationContext(),"버튼 테스트",Toast.LENGTH_LONG).show();
                Intent intent6= new Intent(getApplicationContext(), subActivity6.class);
                startActivity(intent6);
            }
        });

    }
}