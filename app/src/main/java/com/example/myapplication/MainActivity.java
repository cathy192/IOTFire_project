package com.example.myapplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String server_url =
            "https://api.thingspeak.com/channels/990298/fields/1.json?results=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dataTxt = findViewById(R.id.textView);

        fetchData(dataTxt);

    }

    private void fetchData(final TextView dataTxt) {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, server_url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            dataTxt.setText(response.getString("feeds"));

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