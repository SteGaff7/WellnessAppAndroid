package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class EntryActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("UserPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        Setup sharedPrefs if not found
        Intent intent;

//        editor.putBoolean("keepLoggedIn", true);
//        editor.apply();

//        Force refresh for testing
//        editor.putString("lastWellnessEntry", null);
//        editor.apply();

        if (sharedPreferences.contains("user") &&
                sharedPreferences.getBoolean("keepLoggedIn", false)) {
//            And token in date
//            Redirect to home

            intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        } else {
//            Redirect to login page
            intent = new Intent(getApplicationContext(), UserLoginActivity.class);
            startActivity(intent);
        }
    }
}
