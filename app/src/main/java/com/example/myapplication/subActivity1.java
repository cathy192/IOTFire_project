package com.example.myapplication;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.Map;

//Json 데이터 저장할 구조체
class Tm{
    public String date;
    public String time;
    public String temperature;
    public String humidity;
}

public class subActivity1 extends AppCompatActivity {

    //JSON갑 저장 Array
    final public ArrayList<Tm> fireData=new ArrayList<Tm>();
    //https://api.thingspeak.com/channels/990298/fields/1.json?results=20
    String channelNum="990298";
    String server_url="https://api.thingspeak.com/channels/"+channelNum+"/feeds.json?results=20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);

        fetchData();
    }

    //(2020.05.01)mainActivity에서 로딩중에 받아와서 그 값을 받아와 출력 할 수 있도록 변경할 수 있음 좋겠다 =========================================================

    //Json데이터 가져옴, 화면 초기화
    public void fetchData() {
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
                                fireData.add(tt);
                            }

                            //화면에 Json에서 받아온 날짜 및 센서 값 등 출력
                            TextView textView = findViewById(R.id.textView);
                            printText(fireData, textView);

                            //chart 출력
                            printChart(fireData);

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
    //==================================================================================================================================

    //textView에 원하는 값 출력
    void printText(ArrayList<Tm> fireData, TextView textView){
        String dataTxt="";
        //화면에 출력
        for(int i=0;i<fireData.size();i++){
            dataTxt+="날짜:"+fireData.get(i).date+" 시간:"+fireData.get(i).time+" 온도:"
                    +fireData.get(i).temperature+" 습도:"+fireData.get(i).humidity+"\n";
        }
        textView.setText(dataTxt);
    }

    //받아온 값으로 차트 만들기 -> 차트 온도로 보여주기
    void printChart(ArrayList<Tm> fireData){
        LineChart lineChart=(LineChart) findViewById(R.id.chart);
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
        LineData lineData = new LineData(labels,lineDataSet);

        //색깔 설정
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        lineChart.setData(lineData);
        //lineChart.animateY(5000);

    }

}
