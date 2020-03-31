package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.loadmanager.TestActivites.APITestingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WellnessActivity extends AppCompatActivity {

    private static Button submitButton;
    EditText commentEditText;
    private static final String TAG = APITestingActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness);

//      Prevent auto display of keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        submitButton = findViewById(R.id.wellnessSubmitButton);
        commentEditText = findViewById(R.id.commentEditText);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check radio groups and assign values
                RadioGroup[] radioGroups = new RadioGroup[5];

                RadioGroup sleepRadioGroup = findViewById(R.id.sleepRadioGroup);
                radioGroups[0] = sleepRadioGroup;
                RadioGroup energyRadioGroup = findViewById(R.id.energyRadioGroup);
                radioGroups[1] = energyRadioGroup;
                RadioGroup sorenessRadioGroup = findViewById(R.id.sorenessRadioGroup);
                radioGroups[2] = sorenessRadioGroup;
                RadioGroup moodRadioGroup = findViewById(R.id.moodRadioGroup);
                radioGroups[3] = moodRadioGroup;
                RadioGroup stressRadioGroup = findViewById(R.id.stressRadioGroup);
                radioGroups[4] = stressRadioGroup;

                int[] scores = new int[5];
                boolean allChecked = true;
                int i = 0;
                int total_score = 0;

                for (RadioGroup group : radioGroups) {

                    if (group.getCheckedRadioButtonId() == -1) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Make sure all fields are checked",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        allChecked = false;
                        break;
                    } else {
                        int index = group.indexOfChild(findViewById(group.getCheckedRadioButtonId()));
                        int score = index + 1;
                        scores[i] = score;
                        total_score += score;
                    }
                    i++;
                }
                // If valid then submit
                if (allChecked) {
                    submitScores(scores, total_score);
                }
            }
        }
        );
    }

//    private boolean checkFieldsFilled() {
//        if (dateInput.getText().toString().trim().length() == 0) {
//            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }


    private void submitScores(final int[] scores, final int total_score) {

        JSONObject jsonObj = new JSONObject();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", 0);
        final String token = sharedPreferences.getString("token", null);

//        If token not null do, else try retrieve token first


//      Get current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String formattedDate = df.format(c);

        try {
            jsonObj.put("date", formattedDate);
            jsonObj.put("sleep_score", scores[0]);
            jsonObj.put("energy_score", scores[1]);
            jsonObj.put("soreness_score", scores[2]);
            jsonObj.put("mood_score", scores[3]);
            jsonObj.put("stress_score", scores[4]);
            jsonObj.put("total_score", total_score);

            String comment = commentEditText.getText().toString().trim();
            if (comment.length() != 0) {
                jsonObj.put("comments", comment);
            } else {
                jsonObj.put("comments", null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.0.165:8000/wellness/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(WellnessActivity.this, "This is a response message", Toast.LENGTH_SHORT).show();

//                      Get more data from server to use for summary etc?

//                      Redirect to summary activity with results?
                        if (updateSharedPrefs(formattedDate)) {
                            redirectSummaryActivity(scores);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"Error :" + error.toString());
                        Toast.makeText(WellnessActivity.this, "This is an error message", Toast.LENGTH_SHORT).show();
                    }
                }) {
//          Overwrite the headers
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    private boolean updateSharedPrefs(String formattedDate) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPref", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("lastWellnessEntry", formattedDate);
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void redirectSummaryActivity(int[] scores) {
        Intent intent = new Intent(getApplicationContext(), EntrySummaryActivity.class);
        intent.putExtra("scores", scores);
        startActivity(intent);
        finish();
    }
}
