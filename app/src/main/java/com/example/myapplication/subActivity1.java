package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

//Json 데이터 저장할 구조체
class Tm{
    public String date;
    public String time;
    public String temperature;
    public String humidity;
    public String co;
}

public class subActivity1 extends AppCompatActivity {

    TextView updateTimeText; //최신 업데이트 시간
    ImageButton updateBtn;    //최신 동기화 버튼

    LineChart lineChart1; //온도/습도센서 차트
    LineChart lineChart2;   //가스센서 차트

    ImageButton temHuBtn;
    ImageButton gasBtn;

    //센서값
    TextView temText;
    TextView huText;
    TextView coText;
    TextView lpgText;

    //https://api.thingspeak.com/channels/990298/fields/1.json?results=20
    String channelNum="990298";
    String resultNum="30";
    String server_url="https://api.thingspeak.com/channels/"+channelNum+"/feeds.json?results="+resultNum;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);

        this.InitializeView();
        this.SetListener();

        fetchData();
    }

    @SuppressLint("WrongViewCast")
    public void InitializeView() {
        updateTimeText=(TextView)findViewById(R.id.updateTimeText);
        updateBtn=(ImageButton)findViewById(R.id.updateBtn);

        lineChart1=(LineChart) findViewById(R.id.chart);
        lineChart2=(LineChart) findViewById(R.id.gasChart);

        temText=(TextView)findViewById(R.id.tempText2);
        huText=(TextView)findViewById(R.id.huText2);
        coText=(TextView)findViewById(R.id.COText2);
        lpgText=(TextView)findViewById(R.id.LPGText2);
    }

    public void SetListener(){
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTimeText.setText("");
                lineChart1.clear();
                lineChart2.clear();

                temText.setText("");
                huText.setText("");
                coText.setText("");
                lpgText.setText("");
                fetchData();
            }
        });
    }

    //(2020.05.01)mainActivity에서 로딩중에 받아와서 그 값을 받아와 출력 할 수 있도록 변경할 수 있음 좋겠다 =========================================================

    //Json데이터 가져옴, 화면 초기화
    public void fetchData() {
        //JSON갑 저장 Array
        final ArrayList<Tm> fireData=new ArrayList<Tm>();
        //URL에서 JSON파싱
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, server_url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String dataText="";
                        try{
                            JSONArray jsonArr=new JSONArray(response.getString("feeds"));
                            //Json에서 필요한 값만 추출하여 배열에 저장
                            for(int i=0;i<jsonArr.length();i++){
                                JSONObject tempJ=jsonArr.getJSONObject(i);
                                String time=tempJ.getString("created_at");

                                //fireDate배열에 날짜 및 시간 삽입
                                Tm tt=new Tm();
                                int idx = time.indexOf("T");
                                tt.date=time.substring(0,idx);
                                idx = time.indexOf("Z");
                                tt.time=time.substring(time.lastIndexOf("T")+1,idx);
                                tt.temperature=tempJ.getString("field1");
                                tt.humidity=tempJ.getString("field2");
                                tt.co=tempJ.getString("field3");
                                try {
                                    //습도가 100넘으면 값에 추가시키지 않음=> 이거 예외처리 고쳐야함
                                    fireData.add(tt);
                                }
                                catch (NumberFormatException e){
                                    continue;
                                }
                            }
                            Tm lastData=fireData.get(jsonArr.length()-1);
                            temText.setText(lastData.temperature+"℃");
                            huText.setText(lastData.humidity+"%");
                            float co=Float.valueOf(lastData.co);
                            int intCo= (int) co;
                            coText.setText(String.valueOf(intCo)+"");
                            //lpgText.setText(lastData.lpg+"");

                            //chart 출력
                            printTemperatureChart(fireData);
                            setUpdateTime();

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

                Toast.makeText(subActivity1.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(subActivity1.this).addToRequestQueue(objectRequest);
    }

    void setUpdateTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(date);

        updateTimeText.setText(formatDate);
    }

    //==================================================================================================================================

    //textView에 원하는 값 출력
    void printText(ArrayList<Tm> fireData, TextView textView){
        String dataTxt="";
        //화면에 출력
        for(int i=0;i<fireData.size();i++){
            dataTxt+="날짜:"+fireData.get(i).date+" 시간:"+fireData.get(i).time+" 온도:"
                    +fireData.get(i).temperature+" 습도:"+fireData.get(i).humidity+" CO:"+fireData.get(i).co+"\n";
        }
        textView.setText(dataTxt);
    }

    //받아온 값으로 차트 만들기 -> 차트 온도/습도로 보여주기
    void printTemperatureChart(ArrayList<Tm> fireData){
        String temp;
        float a,b,c,d;

        //차트 값 (y)
        ArrayList<Entry> entries1 = new ArrayList<>();  //온도
        ArrayList<Entry> entries2=new ArrayList<>();    //습도

        ArrayList<Entry> entries3=new ArrayList<>();    //CO
        ArrayList<Entry> entries4=new ArrayList<>();    //LPG

        for(int i=0;i<fireData.size();i++){
            try {
                temp = fireData.get(i).temperature;
                a = Float.valueOf(temp);
                temp=fireData.get(i).humidity;
                b=Float.valueOf(temp);
            }
            catch (NumberFormatException e) {
                a = 0;
                b = 0;
            }

            try{
                temp=fireData.get(i).co;
                c=Float.valueOf(temp);
                //temp=fireData.get(i).lpg;
                //d=Float.valueOf(temp);
            }
            catch (NumberFormatException e){
                c=0;
                d=0;
            }

            // => Entry(x,y)
            entries1.add(new Entry(a,i));
            entries2.add(new Entry(b,i));

            entries3.add(new Entry(c,i));
            //entries4.add(new Entry(d,i));
        }

        LineDataSet lineDataSet1=new LineDataSet(entries1,"온도");
        LineDataSet lineDataSet2=new LineDataSet(entries2,"습도");
        LineDataSet lineDataSet3=new LineDataSet(entries3,"CO");
        //LineDataSet lineDataSet4=new LineDataSet(entries4,"LPG");

        //라벨 (상단)
        ArrayList<String> labels= new ArrayList<String>();
        for(int i=0;i<fireData.size();i++){
            labels.add(fireData.get(i).date+"/"+fireData.get(i).time);
        }

        //색깔 및 곡선 차트
        lineDataSet1.setColors(Collections.singletonList(ColorTemplate.rgb("#ff0000")));
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setDrawCubic(true);

        lineDataSet2.setColors(Collections.singletonList(ColorTemplate.rgb("#00ff00")));
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setDrawCubic(true);

        lineDataSet3.setColors(Collections.singletonList(ColorTemplate.rgb("#0000ff")));
        lineDataSet3.setDrawCircles(false);
        lineDataSet3.setDrawCubic(true);

        //lineDataSet4.setColors(Collections.singletonList(ColorTemplate.rgb("#00ffff")));
        //lineDataSet4.setDrawCircles(false);
        //lineDataSet4.setDrawCubic(true);

        LineData lineData=new LineData(labels);
        lineData.addDataSet(lineDataSet1);
        lineData.addDataSet(lineDataSet2);

        LineData lineData1=new LineData(labels);
        lineData1.addDataSet(lineDataSet3);
        //lineData1.addDataSet(lineDataSet4);

        lineChart1.setData(lineData);
        YAxis yLeft = lineChart1.getAxisLeft();
        YAxis yRight = lineChart1.getAxisRight(); //Y축의 오른쪽면 설정
        yLeft.setDrawGridLines(false);
        yRight.setDrawLabels(false);
        yRight.setDrawAxisLine(false);
        yRight.setDrawGridLines(false);
        //눈금 없애기
        lineChart1.getXAxis().setDrawGridLines(false);

        lineChart2.setData(lineData1);
        YAxis yLeft2=lineChart2.getAxisLeft();
        YAxis yRight2=lineChart2.getAxisRight();
        yLeft2.setDrawGridLines(false);
        yRight2.setDrawLabels(false);
        yRight2.setDrawAxisLine(false);
        yRight2.setDrawGridLines(false);
        lineChart2.getXAxis().setDrawGridLines(false);

    }

}
