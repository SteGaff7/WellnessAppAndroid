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

    RecyclerView recyclerView;
    TextView wellnessEntryErrorTextView;
    private MyAdapter mAdapter;
    private ArrayList<JSONObject> data = new ArrayList<>();

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
//                Listeners are carried out on UI thread
//                Parsing/data processing should be carries out on background thread e.g parse network
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(WellnessEntriesListActivity.this, "This is a response message", Toast.LENGTH_SHORT).show();
//                        Notify on main thread
                        mAdapter.notifyDataSetChanged();;
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

//          Overwrite parse network response, this returns a response object to onResponse
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

//                  Parse on background thread
                    parseResponse(jsonArray);

//                    Start a thread on main thread (Not needed)

//                    WellnessEntriesListActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    });

                    return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

        };
        requestQueue.add(request);
    }

    /**
     * Function that parses volley response object and updates adapter data
     * @param response
     */
    private boolean parseResponse(JSONArray response) throws JSONException {
        JSONObject entry;

//      Clear data set
        data.clear();

//      Add header row
        entry = new JSONObject();
        entry.put("date", "Date");
        entry.put("sleep_score", "Sleep");
        entry.put("energy_score", "Energy");
        entry.put("soreness_score", "Soreness");
        entry.put("mood_score", "Mood");
        entry.put("stress_score", "Stress");
        entry.put("total_score", "Total");
        entry.put("comments", null);

        data.add(entry);

        for (int i=0; i < response.length(); i++) {
            entry = (JSONObject) response.get(i);
            data.add(entry);
        }
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
