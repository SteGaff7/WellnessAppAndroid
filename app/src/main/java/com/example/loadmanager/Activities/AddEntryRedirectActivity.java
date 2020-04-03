package com.example.loadmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.loadmanager.R;

public class AddEntryRedirectActivity extends AppCompatActivity {

    Button viewEntriesRedirectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry_redirect);

        viewEntriesRedirectButton = findViewById(R.id.viewEntriesRedirectButton);

        viewEntriesRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WellnessEntriesListActivity.class);
                startActivity(intent);
            }
        });
    }
}
