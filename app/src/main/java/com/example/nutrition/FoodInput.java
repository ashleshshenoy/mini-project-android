package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FoodInput extends AppCompatActivity {

    Button foodEntrySubmitButton;
    ConstraintLayout foodDetailContainer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_input);
        foodDetailContainer = (ConstraintLayout) findViewById(R.id.foodDetailContainer);
        foodEntrySubmitButton = (Button) findViewById(R.id.foodEntrySubmitButton);


        TextView foodName = new TextView(FoodInput.this);
        foodName.setText(getIntent().getStringExtra("name"));
        foodDetailContainer.addView(foodName);


        foodEntrySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FoodInput.this, calorie.class);
                startActivity(i);
                // save in shared preference
            }
        });

    }
}