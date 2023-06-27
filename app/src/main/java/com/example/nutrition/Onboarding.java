package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.number.Precision;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Onboarding extends AppCompatActivity{

    EditText username, age, height, weight;
    Button detailSubmissionBtn;
    String[] activity = {
            "Little or no excerise",
            "light excerise / sports 1-3 days a week",
            "moderate excerise / sports 3-5 days a week",
            "Hard excerise / sports 6-7 days a week",
            "Very hard / sports & physical job"
    };

    Double[] activityMultiplierValues = {1.2, 1.375, 1.55, 1.725, 1.9};

    String[] gender = {
        "male", "female"
    };

    Spinner activitySpinner;
    Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        detailSubmissionBtn = (Button)findViewById(R.id.OnboardingDetailSubmitButton);
        username = (EditText) findViewById(R.id.usernameTextField);
        age = (EditText) findViewById(R.id.AgeTextField);
        height = (EditText) findViewById(R.id.heightTextFeild);
        weight = (EditText) findViewById(R.id.weightTextField);
        activitySpinner = (Spinner) findViewById(R.id.activitySpinner);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);




        activitySpinner.setOnItemSelectedListener(new ActivitySpinner());
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,activity);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        activitySpinner.setAdapter(aa);







        genderSpinner.setOnItemSelectedListener(new GenderSpinner());
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter gaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender);
        gaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        genderSpinner.setAdapter(gaa);






        detailSubmissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    double bmr =0;
                    int dailyCaloireRequirement;
                    Double bmi = 0d;
                    bmi = Double.parseDouble(weight.getText().toString())
                            / Math.pow(Double.parseDouble(height.getText().toString())/100, 2);

                    if(genderSpinner.getSelectedItem().toString().contentEquals("male")){
                        bmr = 66.5 + (13.75 * Double.parseDouble(weight.getText().toString()))
                                + (5.003 * Double.parseDouble(height.getText().toString()))
                                - (6.75 * Integer.parseInt(age.getText().toString()));
                    }
                    else{
                        bmr = 655.1 + (9.563 * Double.parseDouble(weight.getText().toString()))
                                + (1.850 * Double.parseDouble(height.getText().toString()))
                                - 4.676 * Integer.parseInt(age.getText().toString());

                    }

                    dailyCaloireRequirement = (int)(bmr * activityMultiplierValues[activitySpinner.getSelectedItemPosition()]);

                    DecimalFormat df = new DecimalFormat("#.##");
                    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    // write all the data entered by the user in SharedPreference and apply
                    myEdit.putString("username", username.getText().toString());
                    myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                    myEdit.putFloat("weight", (float) Double.parseDouble(weight.getText().toString()));
                    myEdit.putFloat("height", (float) Double.parseDouble(height.getText().toString()));
                    myEdit.putString("gender", genderSpinner.getSelectedItem().toString());
                    myEdit.putInt("maintainenceCalorie",dailyCaloireRequirement);
                    myEdit.putString("bmi", String.format("%.2f", bmi));
                    myEdit.apply();
                    
                    Intent i = new Intent(Onboarding.this, Goal.class);
                    i.putExtra("bmi", String.format("%.2f", bmi));
                    i.putExtra("height", height.getText().toString());
                    i.putExtra("weight", weight.getText().toString());
                    startActivity(i);
                }
            }
        });





    }

    class GenderSpinner implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            Toast.makeText(v.getContext(), "Your choose :" + gender[position],Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class ActivitySpinner implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            Toast.makeText(v.getContext(), "Your choose :" + activity[position],Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
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