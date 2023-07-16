package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Target extends AppCompatActivity {

    TextView targetStatement;
    TextView targetSubStatement;
    TextView targetCalorieDisplay;
    Button targetCheckButton;

    int timePeriod;
    int dailyCalorieBudget;

    int weightGoal;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this

        setContentView(R.layout.activity_target);


        targetStatement = (TextView) findViewById(R.id.targetStatement);
        targetSubStatement = (TextView) findViewById(R.id.targetSubStatement);
        targetCheckButton = (Button) findViewById(R.id.targetCheckButton);
        targetCalorieDisplay = (TextView) findViewById(R.id.targetCalorieDisplay);
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        weightGoal = getIntent().getIntExtra("goal",0);

        if(weightGoal == 0){
            int weight = getIntent().getIntExtra("weight",0);
            dailyCalorieBudget = sharedPreferences.getInt("maintainenceCalorie", 0);
            targetStatement.setText("To maintain " + weight + " \nyou need to ...");
            targetSubStatement.setText("Eat around");
        }
        else if (weightGoal > 0){
            timePeriod = getIntent().getIntExtra("timeperiod", 0);
            dailyCalorieBudget = getIntent().getIntExtra("calorie", 0);
            targetStatement.setText("To Lose " + weightGoal + " kg in " + timePeriod + " weeks,\nyou need to..." );
            targetSubStatement.setText("Eat less than");
        }
        else{

            timePeriod = getIntent().getIntExtra("timeperiod", 0);
            dailyCalorieBudget = getIntent().getIntExtra("calorie", 0);
            targetStatement.setText("To Gain " + Math.abs(weightGoal) + " kg in " + timePeriod + " weeks,\nyou need to..." );
            targetSubStatement.setText("Eat around");
        }


        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, dailyCalorieBudget);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                targetCalorieDisplay.setText(String.valueOf(animation.getAnimatedValue()));
            }

        });
        animator.setDuration(2500); // here you set the duration of the anim
        animator.start();
        targetCheckButton.setEnabled(true);
        targetCheckButton.setBackgroundColor(Color.parseColor("#3700B3"));






        targetCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Target.this, Home.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });








    }
}