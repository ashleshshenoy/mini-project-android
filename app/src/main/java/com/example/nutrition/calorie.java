package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class calorie extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        ImageView breakfastAddBtn = (ImageView) findViewById(R.id.breakfastAddBtn);
        LinearLayout breakfastContainer = (LinearLayout) findViewById(R.id.breakfastContainer);
        TextView breakfastHelpText = (TextView) findViewById(R.id.breakFastHelpText);

        breakfastAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(calorie.this, search.class);
                startActivity(i);

            }
        });

    }
}