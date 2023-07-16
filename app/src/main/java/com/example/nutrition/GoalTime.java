package com.example.nutrition;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class GoalTime extends AppCompatActivity {

    Button setPerWeekGoalBtn;
    SeekBar perWeekGoalSeekBar;
    TextView timeperiod;
    TextView perWeekGoal;
    TextView paceRecommendationText;
    int weightGoal;
    Double progressPerWeek = 0.5;

    int perDayCalorie;
    int weeks;
    String[] recommendations = {
            "This is a good, sustainable pace to reach your goal weight." ,
            "This is a good pace, but you would need to work a bit harder.",
            "At this pace, it can get tough, but with the right discipline, you can do it.",
            "This pace will require extreme commitment."
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_goal_time);



        // declarations
        setPerWeekGoalBtn = (Button) findViewById(R.id.setPerWeekGoalButton);
        timeperiod = (TextView) findViewById(R.id.timePeriod);
        perWeekGoal =(TextView) findViewById(R.id.perWeekGoalText);
        perWeekGoalSeekBar = (SeekBar) findViewById(R.id.perWeekGoalSeekBar);
        paceRecommendationText = (TextView)findViewById(R.id.paceRecommendationText);





        //initalise intial values
        weightGoal = getIntent().getIntExtra("goal", 0);
        weeks = (int)(Math.abs(weightGoal) / progressPerWeek);
        timeperiod.setText("You will reach your goal in about " + weeks + " weeks" );



        perWeekGoalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressPerWeek =(progress + 1) * 0.25;
                weeks = (int) Math.ceil(Math.abs(weightGoal) / progressPerWeek);
                paceRecommendationText.setText(recommendations[progress]);
                perWeekGoal.setText(progressPerWeek.toString() +  " kg");
                timeperiod.setText("You will reach your goal in about " + weeks + " weeks" );

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                setPerWeekGoalBtn.setBackgroundColor(Color.parseColor("#3700B3"));
                setPerWeekGoalBtn.setEnabled(true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        setPerWeekGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                if(weightGoal > 0) {
                    perDayCalorie = (int) (sharedPreferences.getInt("maintainenceCalorie", 0) - ((7500/7) * progressPerWeek));
                    perDayCalorie = (int)Math.floor(perDayCalorie/100) * 100;
                } else {
                    perDayCalorie = (int) (sharedPreferences.getInt("maintainenceCalorie", 0) + ((7500 / 7) * progressPerWeek));
                    perDayCalorie = (int)Math.ceil(perDayCalorie/100) * 100;

                }
                Double fibre = (perDayCalorie * 0.03)/2;
                Double fat = (perDayCalorie * 0.3)/9;
                Double protein = (perDayCalorie * 0.3)/4;
                Double carbs = (perDayCalorie * 0.37)/4;

                SharedPreferences.Editor editor =  sharedPreferences.edit();
                editor.putInt("calorie", perDayCalorie);
                editor.putString("protein", String.format("%.2f", protein));
                editor.putString("fat", String.format("%.2f", fat));
                editor.putString("fibre", String.format("%.2f", fibre));
                editor.putString("carbs", String.format("%.2f", carbs));
                editor.putString("pace", progressPerWeek+ "");
                editor.putString("timeperiod", weeks + "");
                editor.putString("startdate", new Date().toString());
                editor.commit();
                Intent i = new Intent(GoalTime.this, Target.class);
                i.putExtra("timeperiod", weeks);
                i.putExtra("calorie", perDayCalorie);
                i.putExtra("goal", weightGoal);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }
}