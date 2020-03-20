package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText usernameRegEditText;
    EditText emailRegEditText;
    EditText passwordRegEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        registerButton = findViewById(R.id.registerButton);
        usernameRegEditText = findViewById(R.id.usernameRegEditText);
        emailRegEditText = findViewById(R.id.emailRegEditText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();

//                Nest these
                redirectHome();
//                Will kill parents of this activity too
                finishAffinity();
            }
        });
    }

    private void attemptRegistration() {
        boolean emptyFields = false;
        String toastMessage = "";

        String email = emailRegEditText.getText().toString().trim();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Valid", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Not valid", Toast.LENGTH_LONG).show();
        }


    }

    private void redirectHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
