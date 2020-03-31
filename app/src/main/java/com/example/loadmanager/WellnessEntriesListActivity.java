package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class WellnessEntriesListActivity extends AppCompatActivity {

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness_entries_list);

        String white = Integer.toString(Color.parseColor("#ffffff"));
        String lightGreen = Integer.toString(Color.parseColor("#90ee90"));
        String  red = Integer.toString(Color.parseColor("#CD0000"));

        ArrayList<HashMap> data = new ArrayList<HashMap>();

        HashMap<String, String> innerMap = new HashMap<String, String>();

        innerMap.put("date","Date");
        innerMap.put("sleep","Sleep");
        innerMap.put("energy","Energy");
        innerMap.put("soreness","Soreness");
        innerMap.put("mood","Mood");
        innerMap.put("stress","Stress");
        innerMap.put("total","Total");
        innerMap.put("colour", white);

        data.add(innerMap);
        innerMap = new HashMap<String, String>();

        innerMap.put("date","01-02-2010");
        innerMap.put("sleep","4");
        innerMap.put("energy","3");
        innerMap.put("soreness","5");
        innerMap.put("mood","1");
        innerMap.put("stress","2");
        innerMap.put("total","16");
        innerMap.put("colour", red);

        data.add(innerMap);
        innerMap = new HashMap<String, String>();

        innerMap.put("date","02-02-2010");
        innerMap.put("sleep","4");
        innerMap.put("energy","2");
        innerMap.put("soreness","5");
        innerMap.put("mood","5");
        innerMap.put("stress","4");
        innerMap.put("total","20");
        innerMap.put("colour",lightGreen);

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


        RecyclerView recyclerView = findViewById(R.id.myRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, data);

        recyclerView.setAdapter(adapter);
    }
}
