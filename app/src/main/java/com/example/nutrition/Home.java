package com.example.nutrition;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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