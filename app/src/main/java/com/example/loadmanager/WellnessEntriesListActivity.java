package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WellnessEntriesListActivity extends AppCompatActivity {

    MyAdapter adapter;
    RecyclerView recyclerView;
    TextView wellnessEntryErrorTextView;

    private static final String TAG = WellnessEntriesListActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness_entries_list);
        recyclerView = findViewById(R.id.myRecycleView);
        wellnessEntryErrorTextView = findViewById(R.id.wellnessEntryErrorTextView);

//        Get data and then set condition

        getWellnessEntriesVolleyTask();

        boolean conditon = true;

        if (conditon == true) {

            String white = Integer.toString(Color.parseColor("#ffffff"));
            String lightGreen = Integer.toString(Color.parseColor("#90ee90"));
            String red = Integer.toString(Color.parseColor("#CD0000"));

            ArrayList<HashMap> data = new ArrayList<HashMap>();

            HashMap<String, String> innerMap = new HashMap<String, String>();

            innerMap.put("date", "Date");
            innerMap.put("sleep", "Sleep");
            innerMap.put("energy", "Energy");
            innerMap.put("soreness", "Soreness");
            innerMap.put("mood", "Mood");
            innerMap.put("stress", "Stress");
            innerMap.put("total", "Total");
            innerMap.put("colour", white);

            data.add(innerMap);
            innerMap = new HashMap<String, String>();

            innerMap.put("date", "01-02-2010");
            innerMap.put("sleep", "4");
            innerMap.put("energy", "3");
            innerMap.put("soreness", "5");
            innerMap.put("mood", "1");
            innerMap.put("stress", "2");
            innerMap.put("total", "16");
            innerMap.put("colour", red);

            data.add(innerMap);
            innerMap = new HashMap<String, String>();

            innerMap.put("date", "02-02-2010");
            innerMap.put("sleep", "4");
            innerMap.put("energy", "2");
            innerMap.put("soreness", "5");
            innerMap.put("mood", "5");
            innerMap.put("stress", "4");
            innerMap.put("total", "20");
            innerMap.put("colour", lightGreen);

            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);
            data.add(innerMap);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyAdapter(this, data);

            recyclerView.setAdapter(adapter);
        } else {
//            Add button to redirect to wellness page
            wellnessEntryErrorTextView.setText("This is an error message e.g no data");
            wellnessEntryErrorTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void getWellnessEntriesVolleyTask() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", 0);
        final String token = sharedPreferences.getString("token", null);


        //      Use uri builder to customize GET requests
        Uri uri = new Uri.Builder()
                .scheme("http")
                .encodedAuthority("192.168.0.165:8000")
                .path("wellness/")
                .appendQueryParameter("date", "2020-03-31")
                .build();

//        Uri uri = new Uri.Builder()
//                .scheme("http")
//                .encodedAuthority("192.168.0.53:8000")
//                .path("wellness/")
//                .appendQueryParameter("user", "7")
//                .build();

        System.out.println("*********************" + uri.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                uri.toString(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(WellnessEntriesListActivity.this, "This is a response message", Toast.LENGTH_SHORT).show();
                        Toast.makeText(WellnessEntriesListActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"Error :" + error.toString());
                        Toast.makeText(WellnessEntriesListActivity.this, "This is an error message", Toast.LENGTH_SHORT).show();
                    }
                }) {
            //          Overwrite the headers
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
