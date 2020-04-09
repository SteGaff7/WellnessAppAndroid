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
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WellnessEntriesListActivity extends AppCompatActivity {

    private static final String TAG = WellnessEntriesListActivity.class.getName();

    RecyclerView recyclerView;
    TextView wellnessEntryErrorTextView;
    LineChart chart;
    private MyAdapter mAdapter;
    private ArrayList<JSONObject> mAdapterData = new ArrayList<>();
//    private ArrayList<String> dateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness_entries_list);

//        chart = findViewById(R.id.lineChart);
//
//        List<Entry> entries = new ArrayList<Entry>();
//        for (int i=0; i<7; i++) {
//            entries.add(new Entry(i, 7-i));
//        }
//
//        dateList.add("01-01-2000");
//        dateList.add("02-01-2000");
//        dateList.add("03-01-2000");
//        dateList.add("04-01-2000");
//        dateList.add("05-01-2000");
//        dateList.add("06-01-2000");
//        dateList.add("07-01-2000");
//
//
//        YAxis yAxisRight = chart.getAxisRight();
//        yAxisRight.setPosition(null);
//
//
//        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f); // only intervals of 1 day
//        xAxis.setLabelCount(7);
//        xAxis.setValueFormatter(xAxisFormatter);
//
//
//        LineDataSet dataSet = new LineDataSet(entries, "Weekly score");
////        dataSet.setColor();
//
//
//        LineData lineData = new LineData(dataSet);
//        chart.setData(lineData);
//        chart.setDescription(null);
////        Refresh chart
//        chart.invalidate();






//        Initialize adapter with no data set
        mAdapter = new MyAdapter(getApplicationContext(), mAdapterData);

        recyclerView = findViewById(R.id.myRecycleView);
//        Set empty adapter
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        wellnessEntryErrorTextView = findViewById(R.id.wellnessEntryErrorTextView);

        getWellnessEntriesVolleyTask();
    }

//    public class DayAxisValueFormatter extends ValueFormatter {
//        private final BarLineChartBase<?> chart;
//        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
//            this.chart = chart;
//        }
//        @Override
//        public String getFormattedValue(float value) {
//            return dateList.get((int) value);
//        }
//    }



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

//      Chart data
        ArrayList<String> dateList = new ArrayList<String>(response.length());
        List<Entry> chartEntries = new ArrayList<Entry>(response.length());

//      Clear adapter data set
        mAdapterData.clear();

        JSONObject wellnessEntry;

//      Add header row
        wellnessEntry = new JSONObject();
        wellnessEntry.put("date", "Date");
        wellnessEntry.put("sleep_score", "Sleep");
        wellnessEntry.put("energy_score", "Energy");
        wellnessEntry.put("soreness_score", "Soreness");
        wellnessEntry.put("mood_score", "Mood");
        wellnessEntry.put("stress_score", "Stress");
        wellnessEntry.put("total_score", "Total");
        wellnessEntry.put("comments", null);

        mAdapterData.add(wellnessEntry);


        for (int i=0; i < response.length(); i++) {
            wellnessEntry = (JSONObject) response.get(i);
            mAdapterData.add(wellnessEntry);

//          Add to chart data
            String dateStr = (String) wellnessEntry.get("date");
            String formattedDate = formatDate(dateStr);

            float total_score = (float) wellnessEntry.getInt("total_score");

            dateList.add(formattedDate);
            chartEntries.add(new Entry((float) i, total_score));
        }

        for (String s : dateList) {
            System.out.println(s);
        }

        for (Entry s : chartEntries) {
            System.out.println(s.getX() + " - " + s.getY());
        }

        generateChart(chartEntries, dateList);
        return true;
    }

    private String formatDate(String dateStr) {

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateFormatted;
        try {
            Date parsedDate = inputDateFormat.parse(dateStr);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("E", Locale.getDefault());
            dateFormatted = outputDateFormat.format(parsedDate);
            return dateFormatted;
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    private void generateChart(List<Entry> entries, List<String> dateList) {

        chart = findViewById(R.id.lineChart);
//      Sublist of last 7 days max
        List<Entry> weekEntries;
        List<String> weekDates;

        if (entries.size() > 7) {
            weekEntries = new ArrayList<Entry>(entries.subList(0,7));
            weekDates = new ArrayList<String>(dateList.subList(0,7));
        } else {
            weekEntries = entries;
            weekDates = dateList;
        }

        List<Entry> reversedWeekEntries = new ArrayList<Entry>();
        List<String> reversedDateList = new ArrayList<String>();

//      This is a pain - x values must be in increasing order
//      Flip data so 7 days ago is at index 0 etc
        for (int i=weekEntries.size()-1; i>=0; i--) {
            reversedWeekEntries.add(new Entry((float) weekEntries.size() - (i+1), entries.get(i).getY()));
        }
//        Flip date data so 7 days ago is at index 0 etc
        for (int i=weekDates.size()-1; i>=0; i--) {
            reversedDateList.add(weekDates.get(i));
        }

        for(String s: reversedDateList)
            System.out.println(s);

        for(Entry e: reversedWeekEntries)
            System.out.println(e.getX());



//      Nested Value Formatter class
        class DayAxisValueFormatter extends ValueFormatter {
            private final BarLineChartBase<?> chart;
            private List<String> dateList;

//          Constructor
            private DayAxisValueFormatter(BarLineChartBase<?> chart, List<String> dateList) {
                this.chart = chart;
                this.dateList = dateList;
            }

            @Override
            public String getFormattedValue(float value) {
                return dateList.get((int) value);
            }
        }


//      Axes formatting
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setPosition(null);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart, reversedDateList);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
//      Show 7 labels max
        xAxis.setLabelCount(reversedWeekEntries.size());
        xAxis.setValueFormatter(xAxisFormatter);


        LineDataSet dataSet = new LineDataSet(reversedWeekEntries, "Daily total score");
//        dataSet.setColor();


        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setDescription(null);
//        Refresh chart
        chart.invalidate();
    }


    private void displayTextView() {
//      Add button to redirect to wellness page if no entry for today else problem so display a diff error
        wellnessEntryErrorTextView.setText("This is an error message e.g no data");
        wellnessEntryErrorTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}
