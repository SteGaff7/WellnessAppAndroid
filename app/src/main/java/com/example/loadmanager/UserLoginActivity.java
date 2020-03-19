package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserLoginActivity extends AppCompatActivity {

    EditText usernameLoginEditText;
    EditText passwordLoginEditText;

    Button signInButton;
    Button registerActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        registerActivityButton = findViewById(R.id.registerActivityButton);

        registerActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserRegisterActivity.class);
//                Start for result & finish if successful?
                startActivity(intent);
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attemptLogin()) {
//                    Redirect to home
                    ;
                }
            }
        });
    }

    private boolean attemptLogin() {
        boolean emptyFields = false;
        String toastMessage = "";

        if (usernameLoginEditText.getText().toString().trim().length() == 0) {
            emptyFields = true;
            toastMessage += "Username must not be empty\n";
        }
        if (passwordLoginEditText.getText().toString().trim().length() == 0) {
            emptyFields = true;
            toastMessage += "Password must not be empty\n";
        }

        if (emptyFields) {
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            return false;
        }

        String username = usernameLoginEditText.getText().toString();
        String password = passwordLoginEditText.getText().toString();

//        Create login volley task

        return true;
    }
}
