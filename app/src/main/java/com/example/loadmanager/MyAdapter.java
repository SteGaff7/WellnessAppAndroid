package com.example.loadmanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.loadmanager.R.color.lightGreen;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

//    Could use JSONObject here instead of List?
    private List<HashMap> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mCLickListener;

//    Pass data to constructor and give context
    public MyAdapter(Context context, List<HashMap> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

//    Inflate row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.wellness_entry_recycler_item, parent, false);
        return new ViewHolder(view);
    }

//    Binds data to each view within each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Use String[] instead of map in time
        HashMap map = mData.get(position);
        String date = (String) map.get("date");
        String sleep = (String) map.get("sleep");
        String energy = (String) map.get("energy");
        String soreness = (String) map.get("soreness");
        String mood = (String) map.get("mood");
        String stress = (String) map.get("stress");
        String total = (String) map.get("total");
        String color = (String) map.get("color");

        String dateFormatted = "";

//        First row
        if (position == 0) {
            dateFormatted = date;
        } else {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date parsedDate = inputDateFormat.parse(date);
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
                dateFormatted = outputDateFormat.format(parsedDate);
            } catch (ParseException e) {
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

//        int color = Color.parseColor("#90ee90");

        int intColor = Integer.parseInt(color);

        holder.wellnessEntry.setBackgroundColor(intColor);

    }

//    Total number of rows
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
