package com.example.nutrition;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends AppCompatActivity {


    ImageView waterIncrementButton, waterDecrementButton;
    TextView waterStatus, waterDetailedStatus;

    int waterIntake=15;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        SharedPreferences sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
        actionBar.setTitle("Hey " + sh.getString("username", "").toString() + " !");
        setContentView(R.layout.activity_home);
        SharedPreferences waterSp = getSharedPreferences("WaterLog", MODE_PRIVATE);
        SharedPreferences.Editor waterEdit  = waterSp.edit();
        waterDecrementButton = (ImageView) findViewById(R.id.waterDecrementButton);
        waterIncrementButton = (ImageView)findViewById(R.id.waterIncrementButton);
        waterStatus = (TextView)findViewById(R.id.waterStatus);
        waterDetailedStatus = (TextView) findViewById(R.id.waterDetailedStatus);
        View calorieTile = (View) findViewById(R.id.calorieTile);
        View weightTile = (View) findViewById(R.id.weightTile);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        String date = sdf.format(new Date());
        Toast.makeText(this, "" + waterSp.getInt(date,17), Toast.LENGTH_SHORT).show();
        waterIntake = waterSp.getInt(date,0);




        if(waterIntake == 15) waterIncrementButton.setEnabled(false);
        if(waterIntake == 0 ) waterDecrementButton.setEnabled(false);
        waterStatus.setText(waterIntake + " of " + " 15 cups");
        if(waterIntake == 15)
            waterDetailedStatus.setText("Well done! be hydrated everyday");
        else
            waterDetailedStatus.setText( 15 - waterIntake + " more cups to go !");



        calorieTile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent in = new Intent(Home.this, calorie.class);
                 startActivity(in);
             }
         });

        weightTile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent in = new Intent(Home.this, weight.class);
                 startActivity(in);
             }
        });


        waterDecrementButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                waterIntake -=1;
                 if(waterIntake == 0 ) waterDecrementButton.setEnabled(false);
                 if(waterIntake == 14) waterIncrementButton.setEnabled(true);
                 waterStatus.setText(waterIntake + " of " + " 15 cups");
                waterDetailedStatus.setText( 15 - waterIntake + " more cups to go !");
                waterEdit.putInt(sdf.format(new Date()), waterIntake);
                waterEdit.commit();
             }
         });


        waterIncrementButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 waterIntake +=1;
                 waterStatus.setText(waterIntake + " of " + " 15 cups");
                 if(waterIntake == 15)
                     waterDetailedStatus.setText("Well done! be hydrated everyday");
                 else
                     waterDetailedStatus.setText( 15 - waterIntake + " more cups to go !");
                 if(waterIntake == 15) waterIncrementButton.setEnabled(false);
                 if(waterIntake == 1) waterDecrementButton.setEnabled(true);
                 waterEdit.putInt(sdf.format(new Date()), waterIntake);
                 waterEdit.commit();

             }
        });





    }


}