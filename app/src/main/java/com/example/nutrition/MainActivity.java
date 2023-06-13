package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStartedBtn = (Button) findViewById(R.id.getStartedButton);
        CheckBox termsAndConditions = (CheckBox) findViewById(R.id.termsAndConditionCheckBox);

        getStartedBtn.setEnabled(false);
        termsAndConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) getStartedBtn.setEnabled(true);
                else getStartedBtn.setEnabled(false);
            }
        });


        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, Onboarding.class);
                startActivity(in);

            }
        });


    }

}