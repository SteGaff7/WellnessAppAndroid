package com.example.loadmanager.TestActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.loadmanager.R;
import com.example.loadmanager.Fragments.CustomDF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class APITestingActivity extends AppCompatActivity {

//    ADD LATER - PREVENTS BUGS ON ROTATE
//    ALSO HANDLE ON EXIT etc

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//    ...
//        // Retain this Fragment across configuration changes in the host Activity.
//        setRetainInstance(true);
//    }


    private static TextView textViewAPITestResults;
    private static final String TAG = APITestingActivity.class.getName();
    SharedPreferences sharedPreferences;

    NumberPicker picker1;
    NumberPicker picker2;

    Button plusButton;
    Button minusButton;

    Button buttonDF;

    TextView numberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitesting);

        final FragmentManager fm = getSupportFragmentManager();

        buttonDF = findViewById(R.id.buttonDF);
        buttonDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDF dFragment = new CustomDF(0, new HashMap<String, String>());
                dFragment.show(fm, "Custom Dialog Fragment");
            }
        });


        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);

        numberText = findViewById(R.id.numberText);

        numberText.setText("3");

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value;
                String strValue = (String) numberText.getText();
                value = Integer.parseInt(strValue);

                if (!(value >= 5)) {
                    value++;
                    numberText.setText(Integer.toString(value));
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot increment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value;
                String strValue = (String) numberText.getText();
                value = Integer.parseInt(strValue);

                if (!(value <= 1)) {
                    value--;
                    numberText.setText(Integer.toString(value));
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot decrement", Toast.LENGTH_SHORT).show();
                }
            }
        });



        picker1 = findViewById(R.id.picker1);
        picker2 = findViewById(R.id.picker2);

        picker1.setMaxValue(5);
        picker1.setMinValue(1);
        picker1.setWrapSelectorWheel(false);

        sharedPreferences = getSharedPreferences("UserPref", 0);

        textViewAPITestResults = findViewById(R.id.textViewAPITestResults);

//      Shared preferences test
        Button prefTestButton = findViewById(R.id.prefTestButton);

        prefTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPref();
            }
        });

//      Check Auth
        Button checkAuthButton = findViewById(R.id.checkAuthButton);
        checkAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAuthentication();
            }
        });

//      Get Specific User Test
        Button GETUserButton = findViewById(R.id.buttonGETUser);

        GETUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GETUserVolleyTask();
            }
        });

//      Get All Users Test
        Button GETAllUsersButton = findViewById(R.id.buttonGETAll);

        GETAllUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GETAllUsersVolleyTask();
            }
        });

        Button buttonPOSTObjCorrect = findViewById(R.id.buttonPOSTObjCorrect);

        buttonPOSTObjCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POSTObjCorrectVolleyTask();
            }
        });

//      Post Object Test
        Button POSTObjButton = findViewById(R.id.buttonPOSTObj);

        POSTObjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POSTObjVolleyTask();
            }
        });

        Button userButton = findViewById(R.id.userButton);

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPostTask();
            }
        });

//      Serialization test
        Button buttonSerializationTest = findViewById(R.id.buttonSerializationTest);

        buttonSerializationTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serializationTest();
            }
        });

//        Login Test
        Button LoginButton = findViewById(R.id.buttonLoginTest);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginVolleyTask();
            }
        });

//      Async Methods of POST & GET
//      Get JSON Async Test
        Button GETJsonAsync = findViewById(R.id.buttonGETAsync);

        GETJsonAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL apiURL;
                try {
                    apiURL = new URL("https://dummy.restapiexample.com/api/v1/employees");
                    GetJSONAsyncTask getJSON = new GetJSONAsyncTask(apiURL);
                    getJSON.execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

//      Post JSON Async HTTP Connection Test
        Button POSTJsonAsync = findViewById(R.id.buttonPOSTAsync);

        POSTJsonAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkAsyncTask task = new NetworkAsyncTask();
                URL url;
                try {
                    url = new URL("http://192.168.0.53:8000/send_data/");
                    task.execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkAuthentication() {
        String url = "http://192.168.0.53:8000/check_auth/";

        final String token = sharedPreferences.getString("token", null);
        System.out.println("******************************" + token);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
//        GET request for this view
        if (token != null) {
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            textViewAPITestResults.setText("Response" + response.toString());
                            Toast.makeText(getApplicationContext(), "Authenticated", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "Error :" + error.toString());
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                //          Overwrite the headers
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Token " + token);
                    return headers;
                }
            };

            requestQueue.add(request);
        } else {
            Toast.makeText(getApplicationContext(), "Token Null", Toast.LENGTH_LONG).show();
        }
    }

    private void getSharedPref() {
        String user = sharedPreferences.getString("user", null);
        String token = sharedPreferences.getString("token", null);
        String email = sharedPreferences.getString("email", null);

        textViewAPITestResults.setText("User: " + user + "\nToken: " + token + "\nEmail: " + email);
    }


//  METHODS - Volley
//  Don't perform network ops on UI thread

//  Volley is recommended by devs but not for large downloads
//  Volley automatically runs on separate thread

    private void GETAllUsersVolleyTask() {

        RequestQueue queue = Volley.newRequestQueue(this);

//      Use uri builder to customize GET requests
        Uri uri = new Uri.Builder()
                .scheme("http")
                .encodedAuthority("192.168.0.53:8000")
                .path("wellness/")
                .build();


//        By default volley lib has string request, JSON request and image request.
//        JSON Object or JSON Array request - Can do both

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                uri.toString(),
                null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                textViewAPITestResults.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        queue.add(request);
    }

    private void GETUserVolleyTask() {

        RequestQueue queue = Volley.newRequestQueue(this);

        Uri uri = new Uri.Builder()
                .scheme("http")
                .encodedAuthority("192.168.0.53:8000")
                .path("wellness/")
                .appendQueryParameter("user", "7")
                .build();

        JsonArrayRequest request =
                new JsonArrayRequest(Request.Method.GET, uri.toString(),
                null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                textViewAPITestResults.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        queue.add(request);
    }

    private void POSTObjCorrectVolleyTask() {

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("user", 77);
            jsonObj.put("date", "2000-03-05");
            jsonObj.put("sleep_score", 1);
            jsonObj.put("energy_score", 2);
            jsonObj.put("soreness_score", 3);
            jsonObj.put("mood_score", 1);
            jsonObj.put("stress_score", 3);
            jsonObj.put("total_score", 10);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.0.53:8000/wellness/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textViewAPITestResults.setText("Response:" + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
                textViewAPITestResults.setText("Response:" + error.toString());
            }
        });

        requestQueue.add(request);
    }

    private void POSTObjVolleyTask() {
        String url = "http://192.168.0.53:8000/send_data/";

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", "stephen");
            jsonObj.put("age", 26);
            jsonObj.put("address", "D16DA33");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

//        jsonRequest is object to be sent, only used with POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                jsonObj,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textViewAPITestResults.setText("Sent" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

//      Additional Map function used with StringRequests and NOT JsonRequests

//    @Override
//    protected Map<String, String> getParams()
//    {
//            Map<String, String>  params = new HashMap<String, String>();
//            params.put("name", "Alif");
//            params.put("domain", "http://itsalif.info");
//
//            return params;
//    }

        requestQueue.add(request);
    }

    private void userPostTask() {
        String url = "http://192.168.0.53:8000/user_api/";

        JSONObject jsonObj = new JSONObject();
        JSONObject innerObj = new JSONObject();
        try {
            innerObj.put("username", "test2");
            innerObj.put("email", "test2@test.com");
            innerObj.put("password", "testpassword");
            jsonObj.put("owner", innerObj);
            jsonObj.put("text", "this is text");
//            jsonObj.put("text", "this is the text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

//        jsonRequest is object to be sent, only used with POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textViewAPITestResults.setText("Sent" + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        requestQueue.add(request);
    }

    private void serializationTest() {
        String url = "http://192.168.0.53:8000/serialization/";

        JSONObject simpleObj = new JSONObject();
        JSONObject commentObj = new JSONObject();
        JSONObject userObj = new JSONObject();

        try {
//            commentObj.put("content", "new content");
//            simpleObj.put("my_id", 6);
//            simpleObj.put("text", "text work");
//            simpleObj.put("comment", commentObj);
            simpleObj.put("username", "test");
            simpleObj.put("text", "another is text");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

//        jsonRequest is object to be sent, only used with POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                simpleObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textViewAPITestResults.setText("Sent" + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        requestQueue.add(request);
    }

    private void LoginVolleyTask() {
        String url = "http://192.168.0.53:8000/api-token-auth/";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject simpleObj = new JSONObject();

        try {
            simpleObj.put("username", "test");
            simpleObj.put("password", "klopi111");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                simpleObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String token = null;
                        try {
                            token = (String) response.get("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestWithToken(token);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"Error :" + error.toString());
                    }
        });
        requestQueue.add(request);
    }

    private void requestWithToken(final String token) {
        String url = "http://192.168.0.53:8000/check_auth/";

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
//        GET request for this view

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textViewAPITestResults.setText("Response" + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        }) {
//          Overwrite the headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }


//    INNER CLASSES

//  3 types used as parameters, progress & result, leave as void if does not apply
//  Developer guide: https://developer.android.com/reference/android/os/AsyncTask
    private class GetJSONAsyncTask extends AsyncTask<Void, Void, String> {

//      If you want to accept only one URL parameter then declare it when object created using constructor
        private URL url;

        public GetJSONAsyncTask(URL url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void...params) {

            try {
//              Create Connection
                HttpsURLConnection myConnection = (HttpsURLConnection) url.openConnection();

                if (myConnection.getResponseCode() == 200) {
//                    System.out.println("Connected Okay");
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody);

                    StringBuffer sb = new StringBuffer();

//                  Method 1 convert to string

//                    BufferedReader reader = new BufferedReader(responseBodyReader);
//
//                    StringBuffer sb = new StringBuffer();
//                    String str;
//
//                    while((str = reader.readLine()) != null) {
//                        sb.append(str);
//                    }


//                  Method 2 use JSON reader
                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    jsonReader.beginObject();
                    if ((jsonReader.nextName()).equals("status")) {
                        sb.append("Status: " + jsonReader.nextString() + "\n");
                    }

                    if ((jsonReader.nextName()).equals("data")) {
                        jsonReader.beginArray();
                        jsonReader.beginObject();

                        if ((jsonReader.nextName()).equals("id")) {
                            sb.append("ID: " + jsonReader.nextString());
                        }
                    }


//                    jsonReader.endObject();

                    jsonReader.close();

//                  Close connection and return string to onPostExecute()
                    myConnection.disconnect();
                    return sb.toString();

                } else {
                    System.out.println("Error somewhere");
                    throw new RuntimeException("Error");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textViewAPITestResults.setText(result);
        }
    }


    private class NetworkAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            URL url = urls[0];
            String response = "Not connected";

            try {

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");

                String jsonInputString = "{\"name\":\"stephen\"}";

                try {
                    OutputStream os = connection.getOutputStream();
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                if (connection.getResponseCode() == 200) {
//                    InputStream responseBody = connection.getInputStream();
//                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody);
//                    BufferedReader reader = new BufferedReader(responseBodyReader);

//                StringBuffer sb = new StringBuffer();
//                    String str;
//
//                    while (( str = reader.readLine()) != null) {
//                        sb.append(str);
//                    }

                connection.disconnect();

//                return sb.toString();
                return "connected";
//                }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            textViewAPITestResults.setText(response);
        }
    }
}
