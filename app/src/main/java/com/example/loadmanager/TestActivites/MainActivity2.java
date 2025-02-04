package com.example.loadmanager.TestActivites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loadmanager.R;
import com.example.loadmanager.Activities.WellnessActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button wellnessActButton = findViewById(R.id.wellnessActButton);

        wellnessActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WellnessActivity.class);
                startActivity(intent);
            }
        });


        Button apiTestingActivity = findViewById(R.id.apiTestingActivity);

        apiTestingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), APITestingActivity.class);
                startActivity(intent);
            }
        });

        Button loginPageButton = findViewById(R.id.loginPageButton);

        loginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
