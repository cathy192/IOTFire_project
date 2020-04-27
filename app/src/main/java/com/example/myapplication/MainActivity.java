package com.example.myapplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Tm{
   public String date;
   public String time;
}

public class MainActivity extends AppCompatActivity {

    //JSON갑 저장 Array
    //생성 날짜 및 시간
    final ArrayList<Tm> fireDate=new ArrayList<Tm>();
    //field1(온도센서)
    final ArrayList<String> temperature = new ArrayList<>();
    String channelNum="990298";

    String server_url =
            "https://api.thingspeak.com/channels/"+channelNum+"/fields/1.json?results=20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dataTxt = findViewById(R.id.textView);

        fetchData(dataTxt);

    }

    private void fetchData(final TextView dataTxt) {
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
                                fireDate.add(tt);

                                //temperature배열에 온도(field1) 삽입
                                temperature.add(tempJ.getString("field1"));

                            }

                            //화면에 출력
                            for(int i=0;i<fireDate.size();i++){
                                dataText+="Date:"+fireDate.get(i).date+" Time:"+fireDate.get(i).time+" temperature:"+temperature.get(i)+"\n";
                            }
                            dataTxt.setText(dataText);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        });

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(objectRequest);

    }
}