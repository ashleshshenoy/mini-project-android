package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddWeightLog extends AppCompatActivity {

    EditText weightLogInput;
    SharedPreferences sh;
    ImageButton weightLogDecrementBtn, weightLogIncrementBtn;
    Button setWeightLogBtn;

    int index ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight_log);
        weightLogInput = findViewById(R.id.weightLogInput);
        weightLogIncrementBtn = findViewById(R.id.weightLogIncrementBtn);
        weightLogDecrementBtn = findViewById(R.id.weightLogDecrementBtn);
        setWeightLogBtn = findViewById(R.id.setWeightLogBtn);
        sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor shEdit = sh.edit();

        index = getIntent().getIntExtra("index", -1);
        if(index < 0)
            weightLogInput.setText(sh.getFloat("weight", 0) + "");
        else
            weightLogInput.setText(getIntent().getStringExtra( "weight"));


        weightLogInput.setEnabled(true);
        weightLogInput.setBackgroundColor(Color.parseColor("#3700B3"));


        weightLogDecrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightLogInput.setText( Double.parseDouble(weightLogInput.getText().toString())- 1 + "");
            }
        });
        weightLogIncrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightLogInput.setText( Double.parseDouble(weightLogInput.getText().toString())+ 1 + "");
            }
        });

        setWeightLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences s = getSharedPreferences("weightLog", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = s.edit();
                String weightLog = s.getString("log", "");
                try {
                    if(weightLog.contentEquals("")){
                        JSONArray weightLogObj = new JSONArray();
                        JSONObject jsObj = new JSONObject();
                        jsObj.put("date",new SimpleDateFormat("dd MMM").format(new Date()));
                        jsObj.put("weight", weightLogInput.getText().toString());
                        weightLogObj.put(jsObj);
                        myEdit.putString("log",weightLogObj.toString());
                        myEdit.commit();
                        shEdit.putFloat("weight", (float) Double.parseDouble(weightLogInput.getText().toString()));
                        shEdit.commit();

                    }else{
                        JSONArray weightLogObj = new JSONArray(weightLog);
                        JSONObject jsObj = new JSONObject();
                        jsObj.put("date",new SimpleDateFormat("dd MMM").format(new Date()));
                        jsObj.put("weight", weightLogInput.getText().toString());
                        if(index  >= 0)
                            weightLogObj.put(index, jsObj);
                        else
                            weightLogObj.put(jsObj);

                        if(index < 0 ||  index == weightLogObj.length() - 1){
                            shEdit.putFloat("weight", (float) Double.parseDouble(weightLogInput.getText().toString()));
                            shEdit.commit();
                        }
                        myEdit.putString("log",weightLogObj.toString());
                        myEdit.commit();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }




                Intent i = new Intent(AddWeightLog.this, weight.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }

        });



    }
}