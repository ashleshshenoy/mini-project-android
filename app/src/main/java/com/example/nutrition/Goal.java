package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class Goal extends AppCompatActivity {

    NumberPicker targetWeightPicker;
    TextView goalText;
    Button setWeightGoalBtn;
    TextView bmiText;

    Double bmi =0d;
    Double height = 0d;
    int weight;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_goal);

        // intent data retreiving ;
        bmi = Double.parseDouble(getIntent().getStringExtra("bmi"));
        weight = (int)Double.parseDouble(getIntent().getStringExtra("weight"));
        height = Double.parseDouble(getIntent().getStringExtra("height"));


        // declartions
        targetWeightPicker = (NumberPicker) findViewById(R.id.targetWeightPicker);
        goalText = (TextView)findViewById(R.id.targetWeightText);
        setWeightGoalBtn = (Button)findViewById(R.id.setPerWeekGoalButton);
        bmiText = (TextView)findViewById(R.id.bmiRecommendationText);


        Toast.makeText(this, "" + weight + " " +  height , Toast.LENGTH_SHORT).show();

        // calculating min max weight values
        int minWeightValue = (int)( 19.1 * Math.pow(height/100, 2));
        int maxWeightValue = (int)(27 * Math.pow(height/100, 2));






        bmiText.setText("your bmi is " + bmi + " Choosing a target weight in range " + minWeightValue + "-"+ maxWeightValue  +
        " is ideal.");






        // target weight
        targetWeightPicker.setMinValue(30);
        targetWeightPicker.setMaxValue(150);
        targetWeightPicker.setValue((int) Math.round(weight));
        targetWeightPicker.setWrapSelectorWheel(false);

        targetWeightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                setWeightGoalBtn.setEnabled(true);
                setWeightGoalBtn.setBackgroundColor(Color.parseColor("#3700B3"));
                if(newValue > weight)
                    goalText.setText("Gain " + ( newValue - weight) + " kg");
                else if (newValue < weight)
                    goalText.setText("Lose " + (weight - newValue) + " kg");
                else
                    goalText.setText("Maintain " + weight + " kg");
            }
        });


        setWeightGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("goal",weight -  targetWeightPicker.getValue());
                myEdit.putInt("targetWeight", targetWeightPicker.getValue());
                myEdit.commit();
                if(weight - targetWeightPicker.getValue() == 0){
                    Intent i = new Intent(Goal.this, Target.class);
                    i.putExtra("goal", weight -  targetWeightPicker.getValue());
                    i.putExtra("weight", weight);
                    startActivity(i);
                }
                Intent i = new Intent(Goal.this, GoalTime.class);
                i.putExtra("goal", weight -  targetWeightPicker.getValue());
                startActivity(i);

            }
        });

    }
    public static Spannable getColoredString(String mString, int colorId) {
        Spannable spannable = new SpannableString(mString);
        spannable.setSpan(new ForegroundColorSpan(colorId), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }





}