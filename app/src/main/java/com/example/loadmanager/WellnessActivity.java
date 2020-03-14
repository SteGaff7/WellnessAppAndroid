package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.IntStream;

public class WellnessActivity extends AppCompatActivity {

    private static Button submitButton;
    private static EditText userIDInput;
    private static EditText dateInput;
    private static TextView wellnessTest;

    private static final String TAG = APITestingActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness);

        submitButton = findViewById(R.id.wellnessSubmitButton);
        userIDInput = findViewById(R.id.userIDInput);
        dateInput = findViewById(R.id.dateInput);
        wellnessTest = findViewById(R.id.wellnessTest);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Check fields are valid


                if (checkFieldsFilled()) {

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
                            wellnessTest.append("not checked");

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
                            wellnessTest.append("checked " + score);
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
        });
    }


    private boolean checkFieldsFilled() {
        if (userIDInput.getText().toString().trim().length() == 0 &&
                dateInput.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void submitScores(int[] scores, int total_score) {

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("user", Integer.parseInt(userIDInput.getText().toString()));
            jsonObj.put("date", dateInput.getText().toString());
            jsonObj.put("sleep_score", scores[0]);
            jsonObj.put("energy_score", scores[1]);
            jsonObj.put("soreness_score", scores[2]);
            jsonObj.put("mood_score", scores[3]);
            jsonObj.put("stress_score", scores[4]);
            jsonObj.put("total_score", total_score);
            jsonObj.put("comments", null);

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
                        wellnessTest.setText("Response: " + response.toString());
                        Toast.makeText(WellnessActivity.this, "This is a response message", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"Error :" + error.toString());
                        wellnessTest.setText("Response: " + error.toString());
                        Toast.makeText(WellnessActivity.this, "This is an error message", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(request);
    }
}
