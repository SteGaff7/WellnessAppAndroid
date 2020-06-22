package com.example.loadmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.loadmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLoginActivity extends AppCompatActivity {

    EditText usernameLoginEditText;
    EditText passwordLoginEditText;

    Button signInButton;
    Button registerActivityButton;

    private int mStatusCode;
    private static final String TAG = UserLoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        registerActivityButton = findViewById(R.id.registerActivityButton);
        signInButton = findViewById(R.id.signInButton);

        usernameLoginEditText = findViewById(R.id.usernameLoginEditText);
        passwordLoginEditText = findViewById(R.id.passwordLoginEditText);

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
                attemptLogin();
                }
        });
    }

    private void attemptLogin() {
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
            return;
        }

        final String username = usernameLoginEditText.getText().toString();
        final String password = passwordLoginEditText.getText().toString();

//        Create login volley task
        loginVolleyTask(username, password);
    }

    private void loginVolleyTask(final String username, final String password) {
//         Change url to match new view
        String url = "http://192.168.0.164:8000/users/api-login-token-auth/";
//        Obtain other info too such as email etc?
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject requestObj = new JSONObject();

        String toastMessage = "";

        try {
            requestObj.put("username", username);
            requestObj.put("password", password);
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
//                          Add parse response method here to clean the data
//                          Get other useful data and save
                            String token;
                            String date_last_wellness;
                            try {
                                token = (String) response.get("token");
//                              Get last wellness entry from server
                                try {
                                    date_last_wellness = (String) response.get("date");
                                } catch (ClassCastException e) {
                                    date_last_wellness = null;
                                }
                                if (saveUserDetails(token, username, date_last_wellness)) {
                                    redirectHome();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                displayError(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                     Tidy up this
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    Log.i(TAG, "Error :" + error.toString());
                    displayError(statusCode);
                }
            })
//            {
//                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
////                Makes status code accessible for on response
//                    mStatusCode = response.statusCode;
//                    return super.parseNetworkResponse(response);
//                }
//            }
            ;
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }
    }

    private void redirectHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayError(String statusCode) {
//        Add in custom error messages based on error response
        Toast.makeText(getApplicationContext(), "Status Code: " + statusCode + "\nLogin Failed, try again", Toast.LENGTH_LONG).show();
        usernameLoginEditText.setText("");
        passwordLoginEditText.setText("");
    }

    private void displayError(Exception e) {
        Toast.makeText(getApplicationContext(), "Exception: " + e.toString() + "\nLogin Failed, try again", Toast.LENGTH_LONG).show();
        usernameLoginEditText.setText("");
        passwordLoginEditText.setText("");
    }

    private boolean saveUserDetails(String token, String username, String date_last_wellness) {

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPref", 0);
            //         Save user details & token in shared prefs
            //        Add in expiration date here too
            //        If box ticked keep logged in then else false
            Toast.makeText(getApplicationContext(), "Token: " + token + "\nUsername: " + username, Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("user", username);
            editor.putBoolean("keepLoggedIn", true);
            editor.putString("token", token);
            editor.putString("lastWellnessEntry", date_last_wellness);
            editor.apply();
            return true;
        } catch (Exception e) {
            displayError(e);
            return false;
        }
    }
}
