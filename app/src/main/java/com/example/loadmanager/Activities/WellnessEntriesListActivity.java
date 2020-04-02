package com.example.loadmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.loadmanager.Adapters.MyAdapter;
import com.example.loadmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WellnessEntriesListActivity extends AppCompatActivity {

    private static final String TAG = WellnessEntriesListActivity.class.getName();

    private MyAdapter mAdapter;
    RecyclerView recyclerView;
    TextView wellnessEntryErrorTextView;
    private JSONArray data = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness_entries_list);

//        Initialize adapter with no data set
        mAdapter = new MyAdapter(getApplicationContext(), data);

        recyclerView = findViewById(R.id.myRecycleView);
//        Set empty adapter
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        wellnessEntryErrorTextView = findViewById(R.id.wellnessEntryErrorTextView);

        getWellnessEntriesVolleyTask();
    }

    private void getWellnessEntriesVolleyTask() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", 0);
        final String token = sharedPreferences.getString("token", null);

        //      Use uri builder to customize GET requests
        Uri uri = new Uri.Builder()
                .scheme("http")
                .encodedAuthority("192.168.0.165:8000")
                .path("wellness/")
//                .appendQueryParameter("date", "2020-03-31")
                .build();


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                uri.toString(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(WellnessEntriesListActivity.this, "This is a response message", Toast.LENGTH_SHORT).show();
//                        Parse and fill recycler view
//                        recyclerViewDisplay();
//                        Check response code?
//                        try {
//                            Clear adapter data
//                        data = new JSONArray();

//                        data = response;
//
//                            mAdapter.notifyDataSetChanged();



////                        Cant be run on worker threads?
//                        mAdapter.notifyDataSetChanged();



//                        data = response;
//                        MyAdapter adapter = new MyAdapter(getApplicationContext(), data);
//                        recyclerView.setAdapter(adapter);



//                        adapter.notifyDataSetChanged();
                        System.out.println(data);

//                         Add in header row

                        System.out.println(data.length());
//                        try {
//                            parseResponse(response);
//                        } catch (JSONException e) {
//                            System.out.println("*************PARSE ERROR");
//                            e.printStackTrace();
//                        }
//                            data = parseResponse(response);


//                        } catch (JSONException e) {
////                            Handle exception better?
//                            Log.i(TAG,"Error :" + e.toString());
//                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"Error :" + error.toString());
                        Toast.makeText(WellnessEntriesListActivity.this, "This is an error message", Toast.LENGTH_SHORT).show();
//                        Parse error and display text view and button
                        displayTextView();
                    }
                }

                )
        {
            //          Overwrite the headers
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Token " + token);
                return headers;
            }


            @Override
            protected Response<JSONArray> parseNetworkResponse (NetworkResponse response){
//                This thread is on background thread
//                Handle parsing logic here?
                try {
                    String jsonString =
                            new String(
                                    response.data,
                                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONArray jsonArray = new JSONArray(jsonString);
                    data = new JSONArray();
                    data = jsonArray;

//                    METHOD 1

//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        public void run() {
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    });

//                    METHOD 2

//                    Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
//
//                    Runnable myRunnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            mAdapter.notifyDataSetChanged();
//                        System.out.print("***********RAN");
//                        }
//                    };
//                    mainHandler.post(myRunnable);

//                    METHOD 3

                    WellnessEntriesListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                    return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

        };
        requestQueue.add(request);
    }

    /**
     * Function that parses volley response object and returns an ArrayList of HashMaps
     * @param response
     */
    private boolean parseResponse(JSONArray response) throws JSONException {

//        data = response;
        System.out.println(data);

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            public void run() {
//                mAdapter.notifyDataSetChanged();
//            }});
//        mAdapter.notifyDataSetChanged();

//        JSONObject entry;
//        HashMap<String, String> innerMap;
//
////        Change HashMap to array in time
////        String[] stringEntry;
//
//        String white = Integer.toString(Color.parseColor("#ffffff"));
//
//        innerMap = new HashMap<>();
////        Hardcode first row - Replace with header row in time?
//
//        innerMap.put("date", "Date");
//        innerMap.put("sleep", "Sleep");
//        innerMap.put("energy", "Energy");
//        innerMap.put("soreness", "Soreness");
//        innerMap.put("mood", "Mood");
//        innerMap.put("stress", "Stress");
//        innerMap.put("total", "Total");
//        innerMap.put("comments", null);
//        innerMap.put("color", white);
//        data.add(0, innerMap);
//
//        for (int i=0; i< response.length(); i++) {
//            entry = (JSONObject) response.get(i);
//
//            String date = (String) entry.get("date");
//            String sleep = Integer.toString((Integer) entry.get("sleep_score"));
//            String energy = Integer.toString((Integer) entry.get("energy_score"));
//            String soreness = Integer.toString((Integer) entry.get("soreness_score"));
//            String mood = Integer.toString((Integer) entry.get("mood_score"));
//            String stress = Integer.toString((Integer) entry.get("stress_score"));
//            String total = Integer.toString((Integer) entry.get("total_score"));
//            String comments;
//            try {
//                comments = (String) entry.get("comments");
//            } catch (ClassCastException e) {
//                comments = null;
//            }
//
//            innerMap = new HashMap<>();
//
//            innerMap.put("date", date);
//            innerMap.put("sleep", sleep);
//            innerMap.put("energy", energy);
//            innerMap.put("soreness", soreness);
//            innerMap.put("mood", mood);
//            innerMap.put("stress", stress);
//            innerMap.put("total", total);
//            innerMap.put("comments", comments);
//
//
//
//            String red = Integer.toString(Color.parseColor("#CD0000"));
//            String orange = Integer.toString(Color.parseColor("#ff9a1c"));
//            String yellow = Integer.toString(Color.parseColor("#f3f43d"));
//            String green = Integer.toString(Color.parseColor("#00c117"));
//
//
//            int totalInt = Integer.parseInt(total);
//            String colorIntStr;
//
//            if (totalInt < 10) {
//                colorIntStr = red;
//            } else if (totalInt < 15) {
//                colorIntStr = orange;
//            } else if (totalInt < 20) {
//                colorIntStr = yellow;
//            } else {
//                colorIntStr = green;
//            }
//
//            innerMap.put("color", colorIntStr);
//            data.add(i+1, innerMap);
//        }
        return true;
    }


    private void displayTextView() {
//      Add button to redirect to wellness page if no entry for today else problem so display a diff error
        wellnessEntryErrorTextView.setText("This is an error message e.g no data");
        wellnessEntryErrorTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

//    private void recyclerViewDisplay() {
//        String white = Integer.toString(Color.parseColor("#ffffff"));
//        String lightGreen = Integer.toString(Color.parseColor("#90ee90"));
//        String red = Integer.toString(Color.parseColor("#CD0000"));
//
////        ArrayList<HashMap> data = new ArrayList<HashMap>();
//
//        HashMap<String, String> innerMap = new HashMap<String, String>();
//
//        innerMap.put("date", "Date");
//        innerMap.put("sleep", "Sleep");
//        innerMap.put("energy", "Energy");
//        innerMap.put("soreness", "Soreness");
//        innerMap.put("mood", "Mood");
//        innerMap.put("stress", "Stress");
//        innerMap.put("total", "Total");
//        innerMap.put("color", white);
//
//        data.add(innerMap);
//        innerMap = new HashMap<String, String>();
//
//        innerMap.put("date", "01-02-2010");
//        innerMap.put("sleep", "4");
//        innerMap.put("energy", "3");
//        innerMap.put("soreness", "5");
//        innerMap.put("mood", "1");
//        innerMap.put("stress", "2");
//        innerMap.put("total", "16");
//        innerMap.put("color", red);
//
//        data.add(innerMap);
//        innerMap = new HashMap<String, String>();
//
//        innerMap.put("date", "02-02-2010");
//        innerMap.put("sleep", "4");
//        innerMap.put("energy", "2");
//        innerMap.put("soreness", "5");
//        innerMap.put("mood", "5");
//        innerMap.put("stress", "4");
//        innerMap.put("total", "20");
//        innerMap.put("color", lightGreen);
//
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//        data.add(innerMap);
//
//
////        adapter = new MyAdapter(this, data);
////        Dataset has updated, notify adapter to update recycler view
//        adapter.notifyDataSetChanged();
//    }
}
