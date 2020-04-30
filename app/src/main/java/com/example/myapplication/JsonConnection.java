package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class JsonConnection {

    //JSON갑 저장 Array
    final public ArrayList<Tm> fireDate=new ArrayList<Tm>();
    //https://api.thingspeak.com/channels/990298/fields/1.json?results=20
    String channelNum="990298";
    String server_url="https://api.thingspeak.com/channels/"+channelNum+"/feeds.json?results=20";

    public JsonObjectRequest fetchData(final TextView dataTxt) {
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
                                fireDate.add(tt);
                            }

                            //화면에 출력
                            for(int i=0;i<fireDate.size();i++){
                                dataText+="날짜:"+fireDate.get(i).date+" 시간:"+fireDate.get(i).time+" 온도:"+fireDate.get(i).temperature+" 습도:"+fireDate.get(i).humidity+"\n";
                            }
                            dataTxt.setText(dataText);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                //(2020.04.28)===========================================================================================
                // Json파일을 받아오지 못하는 등의 에러가 발생하면 어떻게 해야할지 고쳐야함
                // Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                //======================================================================================================
            }
        });

        return objectRequest;

    }

}
