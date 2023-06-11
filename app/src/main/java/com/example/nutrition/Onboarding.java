package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Onboarding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button detailSubmissionBtn = (Button)findViewById(R.id.OnboardingDetailSubmitButton);
        EditText username = (EditText) findViewById(R.id.usernameTextField);
        EditText age = (EditText) findViewById(R.id.AgeTextField);
        EditText height = (EditText) findViewById(R.id.heightTextFeild);
        EditText weight = (EditText) findViewById(R.id.weightTextField);

        detailSubmissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Onboarding.this, Home.class);
                startActivity(in);
            }
        });
    }
}