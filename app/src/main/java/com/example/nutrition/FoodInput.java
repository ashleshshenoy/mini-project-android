package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Iterator;

public class FoodInput extends AppCompatActivity {

    Button foodEntrySubmitButton;
    NumberPicker foodQuantityPicker;
    String foodName;
    Double foodProtienValue;
    Double foodCalorieValue;
    Double foodFatValue;
    Double foodFibreValue;
    Double foodCarbValue;
    String unit;


    TextView foodNamePlaceholder;

    TextView caloriePlaceholder;

    TextView proteinPlaceholder;
    TextView fatPlaceholder;
    TextView fibrePlaceholder;
    TextView carbPlcaeholder;
    String unitAccrom;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this

        //definations
        setContentView(R.layout.activity_food_input);
        foodNamePlaceholder = (TextView) findViewById(R.id.foodNamePlaceholder);
        foodEntrySubmitButton = (Button) findViewById(R.id.foodEntrySubmitButton);
        foodQuantityPicker = (NumberPicker)findViewById(R.id.foodQuantityPicker);
        caloriePlaceholder = (TextView) findViewById(R.id.caloriePlaceholder);
        proteinPlaceholder = (TextView) findViewById(R.id.proteinPlaceholder);
        carbPlcaeholder = (TextView)findViewById(R.id.carbPlaceholder);
        fibrePlaceholder = (TextView) findViewById(R.id.fibrePlaceholder);
        fatPlaceholder = (TextView)findViewById(R.id.fatPlaceholder);




        //food quantity picking
        foodQuantityPicker.setMinValue(1);
        foodQuantityPicker.setMaxValue(100);
        foodQuantityPicker.setWrapSelectorWheel(false);
        foodQuantityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {

                caloriePlaceholder.setText(
                        newValue * 100 + unitAccrom + "\t" +  String.format("%.1f",  foodCalorieValue * newValue) + " cals");
                carbPlcaeholder.setText(String.format( "%.1f",foodCarbValue * newValue) + "g");
                proteinPlaceholder.setText(String.format( "%.1f",foodProtienValue * newValue) + "g");
                fibrePlaceholder.setText(String.format( "%.1f",foodFibreValue * newValue) + "g");
                fatPlaceholder.setText(String.format( "%.1f",foodFatValue * newValue) + "g");
            }
        });


        //load details of food item
        loadDetails();







        //onsubmit events
        foodEntrySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FoodInput.this, calorie.class);
                startActivity(i);
                // save in shared preference
            }
        });

    }

    public void loadDetails(){
        try{

            InputStream iStream = getAssets().open("food.json");
            int size = iStream.available();
            byte[] buffer =  new byte[size];
            iStream.read(buffer);
            String  json = new String(buffer, "UTF-8");
            JSONObject jsonObj = new JSONObject(json);
            foodName = getIntent().getStringExtra("name");
            JSONObject item = (JSONObject) jsonObj.get(foodName);
            foodNamePlaceholder.setText(foodName);
            foodProtienValue = item.getDouble("protein");
            foodCalorieValue = item.getDouble("calorie");
            foodFatValue = item.getDouble("fat");
            foodCarbValue = item.getDouble("carb");
            foodFibreValue  = item.getDouble("fibre");
            unit = item.getString("unit");

            unitAccrom = (unit.contains("g"))? "g" : "ml" ;
            caloriePlaceholder.setText(
                      100 + unitAccrom + "\t" +  String.format("%.1f",  foodCalorieValue) + " cals");

            proteinPlaceholder.setText(foodProtienValue.toString());
            fatPlaceholder.setText(foodFatValue.toString());
            fibrePlaceholder.setText(foodFibreValue.toString());
            carbPlcaeholder.setText(foodCarbValue.toString());


        }catch (Exception e){
            Log.d("error" ,e + "");
            Toast.makeText(this, "" + e , Toast.LENGTH_LONG).show();
        }
    }

}