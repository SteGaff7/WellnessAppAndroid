package com.example.loadmanager.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.loadmanager.R;

import java.util.HashMap;


public class CustomDF extends DialogFragment {

    private int pos;
    private HashMap<String, String> entry;

    private Listener mListener;
    private Button submitButton;
    private Button cancelButton;

    private TextView editTitle;
    private TextView num1;
    private TextView num2;
    private TextView num3;
    private TextView num4;
    private TextView num5;
    private EditText commentsEditText;

    private Button minusButton1;
    private Button minusButton2;
    private Button minusButton3;
    private Button minusButton4;
    private Button minusButton5;

    private Button plusButton1;
    private Button plusButton2;
    private Button plusButton3;
    private Button plusButton4;
    private Button plusButton5;


    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void returnData(HashMap<String, Object> result);
    }

    public CustomDF(int pos, HashMap<String, String> entry) {
        this.pos = pos;
        this.entry = entry;
    }


    //  Use this when entire view of the dialog is going to be defined via custom XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_df, container, false);
        getDialog().setTitle("DialogFragment Tutorial");

        submitButton = rootView.findViewById(R.id.editWellnessEntryDFSubmit);
        cancelButton = rootView.findViewById(R.id.editWellnessEntryDFCancel);

        minusButton1 = rootView.findViewById(R.id.minusButton1);
        minusButton2 = rootView.findViewById(R.id.minusButton2);
        minusButton3 = rootView.findViewById(R.id.minusButton3);
        minusButton4 = rootView.findViewById(R.id.minusButton4);
        minusButton5 = rootView.findViewById(R.id.minusButton5);

        plusButton1 = rootView.findViewById(R.id.plusButton1);
        plusButton2 = rootView.findViewById(R.id.plusButton2);
        plusButton3 = rootView.findViewById(R.id.plusButton3);
        plusButton4 = rootView.findViewById(R.id.plusButton4);
        plusButton5 = rootView.findViewById(R.id.plusButton5);



        editTitle = rootView.findViewById(R.id.editWellnessTitle);
        editTitle.setText("Edit Entry for " + entry.get("date"));

        num1 = rootView.findViewById(R.id.num1);
        num1.setText(entry.get("sleep_score"));
        num2 = rootView.findViewById(R.id.num2);
        num2.setText(entry.get("energy_score"));
        num3 = rootView.findViewById(R.id.num3);
        num3.setText(entry.get("soreness_score"));
        num4 = rootView.findViewById(R.id.num4);
        num4.setText(entry.get("mood_score"));
        num5 = rootView.findViewById(R.id.num5);
        num5.setText(entry.get("stress_score"));

        commentsEditText = rootView.findViewById(R.id.commentsEditTextEditEntry);
        String comments;

        if (entry.containsKey("comments")) {
            comments = entry.get("comments");
            commentsEditText.setText(comments);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int sleep, energy, soreness, mood, stress, total;

        sleep = Integer.parseInt(num1.getText().toString());
        energy = Integer.parseInt(num2.getText().toString());
        soreness = Integer.parseInt(num3.getText().toString());
        mood = Integer.parseInt(num4.getText().toString());
        stress = Integer.parseInt(num5.getText().toString());
        total = sleep + energy + soreness + mood + stress;

        final HashMap<String, Object> newEntry = new HashMap<>();

        newEntry.put("position", pos );
        newEntry.put("sleep_score", sleep);
        newEntry.put("energy_score", energy);
        newEntry.put("soreness_score", soreness);
        newEntry.put("mood_score", mood);
        newEntry.put("stress_score", stress);
        newEntry.put("total_score", total);
//        comments wil be string

//        Do button stuff here
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.returnData(newEntry);
                }

                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
//        Maybe get width of parent?
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


}
