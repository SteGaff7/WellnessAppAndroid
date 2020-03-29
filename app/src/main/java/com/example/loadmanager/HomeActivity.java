package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.loadmanager.TestActivites.MainActivity2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    Button wellnessButton;
    Button oldMainButton;
    Button logoutButton;
    Button wellnessEntriesListActivityButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        wellnessButton = findViewById(R.id.wellnessActivityButton);
        wellnessEntriesListActivityButton = findViewById(R.id.wellnessEntriesListActivityButton);
        oldMainButton = findViewById(R.id.oldMainActivity);
        logoutButton = findViewById(R.id.logoutButton);

//      Assign activities to redirect buttons
        wellnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLastWellnessEntry();
            }
        });

        wellnessEntriesListActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WellnessEntriesListActivity.class);
                startActivity(intent);
            }
        });

        oldMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

//      Function to log current user and redirect to login/home page & finish activity
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getSharedPreferences("UserPref", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
//              Just removes values
                editor.clear();
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkLastWellnessEntry() {
        Intent intent;

        Date current = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String formattedDateToday = df.format(current);

        sharedPreferences = getSharedPreferences("UserPref", 0);
        String lastWellnessEntry = sharedPreferences.getString("lastWellnessEntry", null);

        if (lastWellnessEntry == null || ! lastWellnessEntry.equals(formattedDateToday)) {
            intent = new Intent(getApplicationContext(), WellnessActivity.class);
        } else {
//          Redirect to add entry redirect activity
            intent = new Intent(getApplicationContext(), AddEntryRedirectActivity.class);
        }
        startActivity(intent);
    }
}
