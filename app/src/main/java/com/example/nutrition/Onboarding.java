package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Onboarding extends AppCompatActivity {

    EditText username, age, height, weight;
    Button detailSubmissionBtn;
    String errorText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        detailSubmissionBtn = (Button)findViewById(R.id.OnboardingDetailSubmitButton);
        username = (EditText) findViewById(R.id.usernameTextField);
        age = (EditText) findViewById(R.id.AgeTextField);
        height = (EditText) findViewById(R.id.heightTextFeild);
        weight = (EditText) findViewById(R.id.weightTextField);









        detailSubmissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // write all the data entered by the user in SharedPreference and apply
                    myEdit.putString("username", username.getText().toString());
                    myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                    myEdit.putFloat("weight", (float) Double.parseDouble(weight.getText().toString()));
                    myEdit.putFloat("height", (float) Double.parseDouble(height.getText().toString()));
                    myEdit.apply();

                    Intent in = new Intent(Onboarding.this, Home.class);
                    startActivity(in);
                }
            }
        });
    }
    public Boolean validateForm(){

        if(username.length() > 16 ){
            Toast.makeText(this, "username too large", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(username.length() < 1) {
            Toast.makeText(this, "username too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(age.length() < 1){
            Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(weight.length() <1 ){
            Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(height.length() < 1 ) {
            Toast.makeText(this, "please fill the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    };

}