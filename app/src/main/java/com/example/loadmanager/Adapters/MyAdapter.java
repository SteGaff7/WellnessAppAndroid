package com.example.loadmanager.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loadmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<JSONObject> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mCLickListener;

//    Pass data to constructor and give context
    public MyAdapter(Context context, ArrayList<JSONObject> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

//    Inflate row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_wellness_entry, parent, false);
        return new ViewHolder(view);
    }

//    Binds data to each view within each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resources resources = holder.itemView.getContext().getResources();

        JSONObject entry;
        String date, sleep, energy, soreness, mood, stress, total, comments, dateFormatted;
        date = sleep = energy = soreness = mood = stress = total = comments = dateFormatted = "";

        int intColor = ResourcesCompat.getColor(resources, R.color.white, null);

//      Get relative wellness entry JSON Object
        entry = mData.get(position);

        if (position == 0) {
//          Header row
            try {
                sleep = (String) entry.get("sleep_score");
                energy = (String) entry.get("energy_score");
                soreness = (String) entry.get("soreness_score");
                mood = (String) entry.get("mood_score");
                stress = (String) entry.get("stress_score");
                total = (String) entry.get("total_score");
                dateFormatted = "Date";
            } catch (JSONException e) {
                    e.printStackTrace();
                }
        } else {
            try {
//            Get values
                date = (String) entry.get("date");
                sleep = Integer.toString((Integer) entry.get("sleep_score"));
                energy = Integer.toString((Integer) entry.get("energy_score"));
                soreness = Integer.toString((Integer) entry.get("soreness_score"));
                mood = Integer.toString((Integer) entry.get("mood_score"));
                stress = Integer.toString((Integer) entry.get("stress_score"));
                total = Integer.toString((Integer) entry.get("total_score"));

//            Get comments if any
                try {
                    if (entry.has("comments")) {
                        comments = (String) entry.get("comments");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//              Format date
//              Check date logic below?

                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date parsedDate = inputDateFormat.parse(date);
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                    dateFormatted = outputDateFormat.format(parsedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                Parse total score from string
                int totalInt = Integer.parseInt(total);

                if (totalInt < 10) {
                    intColor = ResourcesCompat.getColor(resources, R.color.red, null);
                } else if (totalInt < 15) {
                    intColor = ResourcesCompat.getColor(resources, R.color.orange, null);
                } else if (totalInt < 20) {
                    intColor = ResourcesCompat.getColor(resources, R.color.yellow, null);
                } else {
                    intColor = ResourcesCompat.getColor(resources, R.color.lightGreen, null);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        holder.dateTextView.setText(dateFormatted);
        holder.sleepTextView.setText(sleep);
        holder.energyTextView.setText(energy);
        holder.sorenessTextView.setText(soreness);
        holder.moodTextView.setText(mood);
        holder.stressTextView.setText(stress);
        holder.totalTextView.setText(total);
        holder.wellnessEntry.setBackgroundColor(intColor);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


//    Inner class that stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateTextView;
        TextView sleepTextView;
        TextView energyTextView;
        TextView sorenessTextView;
        TextView moodTextView;
        TextView stressTextView;
        TextView totalTextView;
        LinearLayout wellnessEntry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView =itemView.findViewById(R.id.wellnessEntryDateTextView);
            sleepTextView = itemView.findViewById(R.id.wellnessEntrySleepTextView);
            energyTextView = itemView.findViewById(R.id.wellnessEntryEnergyTextView);
            sorenessTextView = itemView.findViewById(R.id.wellnessEntrySorenessTextView);
            moodTextView = itemView.findViewById(R.id.wellnessEntryMoodTextView);
            stressTextView = itemView.findViewById(R.id.wellnessEntryStressTextView);
            totalTextView = itemView.findViewById(R.id.wellnessEntryTotalTextView);
            wellnessEntry = itemView.findViewById(R.id.wellnessEntryLinearLayout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ;
        }
    }
}
