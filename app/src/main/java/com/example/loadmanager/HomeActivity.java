package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.loadmanager.TestActivites.MainActivity2;

public class HomeActivity extends AppCompatActivity {

    Button wellnessButton;
    Button oldMainButton;
    Button logoutButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        wellnessButton = findViewById(R.id.wellnessActivityButton);
        oldMainButton = findViewById(R.id.oldMainActivity);
        logoutButton = findViewById(R.id.logoutButton);

//      Assign activities to redirect buttons
        wellnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WellnessActivity.class);
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
}
