package com.example.loadmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loadmanager.R;

public class EntrySummaryActivity extends AppCompatActivity {

    Button viewEntries;
    TextView scoresTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_summary);

        Bundle extras = getIntent().getExtras();
        int[] scores= extras.getIntArray("scores");
        String scoresString = "";

        for (int i: scores) {
            scoresString += " " + i;
        }

        viewEntries = findViewById(R.id.viewEntriesSummrayButton);
        scoresTextView = findViewById(R.id.scoresTextView);

        scoresTextView.setText(scoresString);

        viewEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WellnessEntriesListActivity.class);
                startActivity(intent);
            }
        });
    }
}
