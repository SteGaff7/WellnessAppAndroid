package com.example.loadmanager.TestActivites;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.loadmanager.R;
import com.example.loadmanager.TestActivites.APITestingActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsernameEditText;
    EditText loginPasswordEditText;
    EditText registerUsernameEditText;
    EditText registerPasswordEditText;

    Button loginSubmitButton;
    Button registerSubmitButton;

    SharedPreferences sharedPreferences;

    String username;
    String password;

    private int mStatusCode;
    private static final String TAG = APITestingActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserPref", 0);

        loginUsernameEditText = findViewById(R.id.loginUsernameEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);

        registerUsernameEditText = findViewById(R.id.registerUsernameEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);

        loginSubmitButton = findViewById(R.id.loginSubmitButton);
        registerSubmitButton = findViewById(R.id.registerSubmitButton);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Check not empty, check username, check password valid, return true if so and then token
                login();
            }
        });

        registerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
//    Check fields before sending
        if (registerUsernameEditText.getText().toString().trim().length() == 0 ||
                registerPasswordEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        username = registerUsernameEditText.getText().toString();
        password = registerPasswordEditText.getText().toString();

        RegisterVolleyTask(username, password);
    }

    private void RegisterVolleyTask(String username, String password) {
        String url = "http://192.168.0.53:8000/register/";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject requestObj = new JSONObject();

        try {
            requestObj.put("username", username);
            requestObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String token = null;
                        try {
//                             Save like the login in stuff
                            System.out.println("**************" + response.toString());
                            token = (String) response.get("token");
                            System.out.println("**************" + token);
                            System.out.println("**************" + mStatusCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Parse volley error: https://stackoverflow.com/questions/35841118/how-to-get-error-message-description-using-volley
//                parseVolleyError(error);

                if (error.networkResponse != null) {
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    if (statusCode.equals("400")) {
//                    Do something
                        System.out.println("Status code:" + statusCode);
                    }
                    displayError(statusCode);
                    System.out.println(error.networkResponse.data.toString());
                }
                Log.i(TAG,"Error :" + error.toString());
            }
        })
        {
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                Makes status code accessible for on response
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        }
                ;
        requestQueue.add(request);
    }
//
//    public void parseVolleyError(VolleyError error) {
//        try {
//            String responseBody = new String(error.networkResponse.data, "utf-8");
//            JSONObject data = new JSONObject(responseBody);
//            JSONArray errors = data.getJSONArray("errors");
//            JSONObject jsonMessage = errors.getJSONObject(0);
//            String message = jsonMessage.getString("message");
//            System.out.println("Error: " + message);
//        } catch (JSONException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }


    private void login() {
//        Check everything is valid before sending request
        if (loginUsernameEditText.getText().toString().trim().length() == 0 ||
                loginPasswordEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        username = loginUsernameEditText.getText().toString();
        password = loginPasswordEditText.getText().toString();

        LoginVolleyTask(username, password);
    }

    private void LoginVolleyTask(String username, String password) {
        String url = "http://192.168.0.53:8000/api-token-auth/";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject requestObj = new JSONObject();

        try {
            requestObj.put("username", username);
            requestObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                requestObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("**************" + response.toString());
                        String token = null;
                        try {
                            token = (String) response.get("token");
                            System.out.println("**************" + token + " " + mStatusCode);
                            saveDetails(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String statusCode = String.valueOf(error.networkResponse.statusCode);

                if (statusCode.equals("400")) {
//                    Do something
                    System.out.println("Status code:" + statusCode);
                }
                Log.i(TAG,"Error :" + error.toString());
                System.out.println("******************** status code:" + statusCode + " error: " + error.toString());
//                Call function
                displayError(statusCode);
            }
        })
        {
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                Makes status code accessible for on response
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        }
        ;
        requestQueue.add(request);
    }

    private void saveDetails(String token) {
        Toast.makeText(getApplicationContext(), "Token: " + token, Toast.LENGTH_LONG).show();
//        Save user details
//        Save token
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("user", username);
        editor.putBoolean("keepLoggedIn", true);
        editor.putString("token", token);
        editor.apply();
//        Redirect & kill activity
        finish();
    }

    private void displayError(String statusCode) {
        Toast.makeText(getApplicationContext(), "Status Code: " + statusCode + "\nLogin Failed, try again", Toast.LENGTH_LONG).show();
        loginUsernameEditText.setText("");
        loginPasswordEditText.setText("");
    }
}