package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
