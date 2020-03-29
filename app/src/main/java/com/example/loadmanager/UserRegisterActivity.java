package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegisterActivity extends AppCompatActivity {

    Button registerButton;

    EditText usernameRegEditText;
    EditText emailRegEditText;
    EditText passwordRegEditText;

    private static final String TAG = UserRegisterActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        registerButton = findViewById(R.id.registerButton);
        usernameRegEditText = findViewById(R.id.usernameRegEditText);
        emailRegEditText = findViewById(R.id.emailRegEditText);
        passwordRegEditText= findViewById(R.id.passwordRegEditText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();

//                Nest these
//                redirectHome();
////                Will kill parents of this activity too
//                finishAffinity();
            }
        });
    }

    private void attemptRegistration() {
        boolean invalidFields = false;
        String toastMessage = "";

        final String username = usernameRegEditText.getText().toString().trim();
        final String email = emailRegEditText.getText().toString().trim();
        final String password = passwordRegEditText.getText().toString().trim();

        if (! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toastMessage += "Not a valid email\n";
            invalidFields = true;
        }
        if (username.length() == 0) {
            invalidFields = true;
            toastMessage += "Username must not be empty\n";
        }
        if (password.length() == 0) {
            invalidFields = true;
            toastMessage += "Password must not be empty\n";
        }
        if (password.length() < 8 && password.length() != 0) {
            invalidFields = true;
            toastMessage += "Password must be at least 8 characters\n";
        }

        if (invalidFields) {
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            return;
        }

        registerUserVolleyTask(username, email, password);
    }

    private void registerUserVolleyTask(final String username, final String email, final String password) {
//        Change to match new views
        String url = "http://192.168.0.53:8000/users/register/";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject requestObj = new JSONObject();

        String toastMessage = "";

        try {
            requestObj.put("username", username);
            requestObj.put("password", password);
            requestObj.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            toastMessage += e.getMessage();
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    url,
                    requestObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String token;
                            try {
//                          Save like the login in stuff
                                token = (String) response.get("token");
                                if (saveUserDetails(username, token, email)) {
//                              redirect here?
                                    redirectHome();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                displayError(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                Parse volley error: https://stackoverflow.com/questions/35841118/how-to-get-error-message-description-using-volley
//                parseVolleyError(error);

                    Log.i(TAG, "Error :" + error.toString());
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    displayError(statusCode);
                }
            })
//        {
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
////                Makes status code accessible for on response
//                mStatusCode = response.statusCode;
//                return super.parseNetworkResponse(response);
//            }
//        }
                    ;
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }
    }

    private void displayError(String statusCode) {
//        Add in custom error messages based on error response
        Toast.makeText(getApplicationContext(), "Status Code: " + statusCode + "\nLogin Failed, try again", Toast.LENGTH_LONG).show();
        usernameRegEditText.setText("");
        passwordRegEditText.setText("");
        emailRegEditText.setText("");
    }

    private void displayError(Exception e) {
        Toast.makeText(getApplicationContext(), "Exception: " + e.toString() + "\nLogin Failed, try again", Toast.LENGTH_LONG).show();
        usernameRegEditText.setText("");
        passwordRegEditText.setText("");
        emailRegEditText.setText("");
    }

    private boolean saveUserDetails(String username, String token, String email) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPref", 0);
//          Save user details & token in shared prefs
//          Add in expiration date here too
//          If box ticked keep logged in then else false
            Toast.makeText(getApplicationContext(), "Token: " + token + "\nUsername: " + username + "\nEmail: " + email, Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("user", username);
            editor.putString("email", email);
            editor.putBoolean("keepLoggedIn", true);
            editor.putString("token", token);
            editor.putString("lastWellnessEntry", null);
            editor.apply();
            return true;
        } catch (Exception e) {
            displayError(e);
            return false;
        }
    }

    private void redirectHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
//      Will kill parents of this activity too
        finishAffinity();
    }
}
