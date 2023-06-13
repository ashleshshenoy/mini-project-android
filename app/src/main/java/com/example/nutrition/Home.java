package com.example.nutrition;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;

public class Home extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as it
        //s parameter
        SharedPreferences sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
        actionBar.setTitle("Hey " + sh.getString("username", "").toString() + " !");

        setContentView(R.layout.activity_home);



        View calorieTile = (View) findViewById(R.id.calorieTile);
         View weightTile = (View) findViewById(R.id.weightTile);

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


    }
}