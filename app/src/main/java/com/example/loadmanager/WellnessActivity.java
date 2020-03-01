package com.example.loadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class WellnessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness);

        Button submitButton = findViewById(R.id.wellnessSubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Can get rid of later
                TextView wellnessTest = findViewById(R.id.wellnessTest);
                wellnessTest.setText("");

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

                for (RadioGroup group : radioGroups) {

                    if (group.getCheckedRadioButtonId() == -1) {
                        wellnessTest.append("not checked");

                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Make sure all fields are checked",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        allChecked = false;
                        break;
                    }
                    else {

                        int index = group.indexOfChild(findViewById(group.getCheckedRadioButtonId()));
                        int score = index + 1;
                        scores[i] = score;
                        wellnessTest.append("checked " + score);
                    }
                    i++;
                }

                System.out.println("********************" + Arrays.toString(scores));

                // If valid then submit
                if (allChecked) {
                    // Submit
                    wellnessTest.append("All checked, submitting..." + Arrays.toString(scores));

                    JSONObject jsonData = new JSONObject();
                    try {
                        jsonData.put("sleep", scores[0]);
                        jsonData.put("energy", scores[1]);
                        jsonData.put("soreness", scores[2]);
                        jsonData.put("mood", scores[3]);
                        jsonData.put("stress", scores[4]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    wellnessTest.setText(jsonData.toString());
                }
            }
        });
    }
}
