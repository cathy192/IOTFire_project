package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    Button updateBtn;    //최신 동기화 버튼
    TextView sensorText;  //센서값
    LineChart lineChart1; //온도센서 차트
    LineChart lineChart2; //스도센서 차트

    //https://api.thingspeak.com/channels/990298/fields/1.json?results=20
    String channelNum="990298";
    String resultNum="15";
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

    public void InitializeView() {
        updateTimeText=(TextView)findViewById(R.id.updateTimeText);
        updateBtn=(Button)findViewById(R.id.updateBtn);
        sensorText = findViewById(R.id.sensorText);

        lineChart1=(LineChart) findViewById(R.id.chart);
        lineChart2=(LineChart)findViewById(R.id.chart2);
    }

    public void SetListener(){
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorText.setText("");
                updateTimeText.setText("");
                lineChart1.clear();
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
                                    //String temp=tt.humidity;
                                    //float humidity=Float.valueOf(temp);
                                    //if(humidity>100)
                                    //    continue;
                                    fireData.add(tt);
                                }
                                catch (NumberFormatException e){
                                    continue;
                                }

                            }

                            //화면에 Json에서 받아온 날짜 및 센서 값 등 출력
                            printText(fireData, sensorText);

                            //chart 출력
                            printTemperatureChart(fireData);
                            printHumidityChart(fireData);

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

    //받아온 값으로 차트 만들기 -> 차트 온도로 보여주기
    void printTemperatureChart(ArrayList<Tm> fireData){
        String temp;
        float a;

        //차트 값 (y)
        ArrayList<Entry> entries = new ArrayList<>();

        for(int i=0;i<fireData.size();i++){
            try {
                temp = fireData.get(i).temperature;
                a = Float.valueOf(temp);
            }
            catch (NumberFormatException e){
                a=0;
            }
            // => Entry(x,y)
            entries.add(new Entry(a,i));
        }

        LineDataSet lineDataSet=new LineDataSet(entries,"# of calls");

        //라벨 (상단)
        ArrayList<String> labels= new ArrayList<String>();
        for(int i=0;i<fireData.size();i++){
            labels.add(fireData.get(i).date+"/"+fireData.get(i).time);
        }

        //색깔 및 곡선 차트
        lineDataSet.setColors(Collections.singletonList(ColorTemplate.rgb("#ff0000")));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCubic(true);

        LineData lineData = new LineData(labels,lineDataSet);
        //lineData.setDrawValues(false);

        YAxis y = lineChart1.getAxisLeft();
        y.setAxisMaxValue(100);
        y.setAxisMinValue(0);

        YAxis yAxisRight = lineChart1.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        lineChart1.setData(lineData);

        //눈금 없애기
        lineChart1.getXAxis().setDrawGridLines(false);
        lineChart1.getAxisLeft().setDrawGridLines(false);
        lineChart1.getAxisRight().setDrawGridLines(false);

        //배경색
        //lineChart1.setBackgroundColor(Color.WHITE);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.RED);
    }

    //차트 습도로 보여주기
    void printHumidityChart(ArrayList<Tm> fireData){
        String temp;
        float a;

        //차트 값 (y)
        ArrayList<Entry> entries = new ArrayList<>();

        for(int i=0;i<fireData.size();i++){
            try {
                temp = fireData.get(i).humidity;
                a = Float.valueOf(temp);
            }
            catch (NumberFormatException e){
                a=0;
            }
            // => Entry(x,y)
            entries.add(new Entry(a,i));
        }

        LineDataSet lineDataSet=new LineDataSet(entries,"# of calls");

        //라벨 (상단)
        ArrayList<String> labels= new ArrayList<String>();
        for(int i=0;i<fireData.size();i++){
            labels.add(fireData.get(i).date+"/"+fireData.get(i).time);
        }

        //색깔 및 곡선 차트
        lineDataSet.setColors(Collections.singletonList(ColorTemplate.rgb("#0000ff")));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCubic(true);

        LineData lineData = new LineData(labels,lineDataSet);

        YAxis y = lineChart2.getAxisLeft();
        y.setAxisMaxValue(100);
        y.setAxisMinValue(0);

        lineChart2.setData(lineData);

        lineChart2.getXAxis().setDrawGridLines(false);
        lineChart2.getAxisLeft().setDrawGridLines(false);
        lineChart2.getAxisRight().setDrawGridLines(false);

        YAxis yAxisRight = lineChart2.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        //배경색
        //lineChart1.setBackgroundColor(Color.WHITE);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.BLUE);

    }

}
