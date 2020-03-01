package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitesting);

        textViewAPITestResults = findViewById(R.id.textViewAPITestResults);

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

//      Post Object Test
        Button POSTObjButton = findViewById(R.id.buttonPOSTObj);

        POSTObjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POSTObjVolleyTask();
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
