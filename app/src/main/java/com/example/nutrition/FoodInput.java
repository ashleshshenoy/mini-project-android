package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    String timing;
    String date;

    TextView foodNamePlaceholder;

    TextView caloriePlaceholder;

    TextView proteinPlaceholder;
    TextView fatPlaceholder;
    TextView fibrePlaceholder;
    TextView carbPlcaeholder;
    String unitAccrom;
    int totalUnits;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this


        timing = getIntent().getStringExtra("timing");
        date = getIntent().getStringExtra("date");


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
        foodEntrySubmitButton = (Button)findViewById(R.id.foodEntrySubmitButton);



        //food quantity picking
        foodQuantityPicker.setMinValue(1);
        foodQuantityPicker.setMaxValue(100);
        foodQuantityPicker.setWrapSelectorWheel(false);

        foodQuantityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {

                caloriePlaceholder.setText(
                        newValue * totalUnits + unitAccrom + "\t" +  String.format("%.1f",  foodCalorieValue * newValue) + " cals");
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
                submitData();
                Intent i = new Intent(FoodInput.this, calorie.class);
                i.putExtra("date", date);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            if(unit.contains("ml")) {
                unitAccrom = "ml";
                totalUnits = 100;
            }
            else if(unit.contains("g")) {
                unitAccrom = "g";
                totalUnits = 100;
            }
            else {
                unitAccrom = " peice    ";
                totalUnits = 1;
            }
            caloriePlaceholder.setText(
                      totalUnits + unitAccrom + "\t" +  String.format("%.1f",  foodCalorieValue) + " cals");

            proteinPlaceholder.setText(foodProtienValue.toString());
            fatPlaceholder.setText(foodFatValue.toString());
            fibrePlaceholder.setText(foodFibreValue.toString());
            carbPlcaeholder.setText(foodCarbValue.toString());


        }catch (Exception e){
            Log.d("error" ,e + "");
        }
    }


    public void submitData(){
        // date + ENUM.value

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String key = date ;
        SharedPreferences sharedPreferences = getSharedPreferences("FoodLog", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        JSONObject js  = new JSONObject();
        try {
        //check if json already exits
            if(sharedPreferences.contains(key)){

                    js = new JSONObject(sharedPreferences.getString(key, ""));
                    JSONObject entry;
                    if(js.has(timing)) {
                         entry =  js.getJSONObject(timing);
                         if(entry.has(foodName)){
                            JSONObject item = entry.getJSONObject(foodName);
                            item.put("quantity", item.getInt("quantity") +  foodQuantityPicker.getValue());
                        }else{
                             JSONObject item = new JSONObject();
                            item.put("name", foodName);
                            item.put("quantity", foodQuantityPicker.getValue());
                            entry.put(foodName , item);
                        }
                    }else{
                        entry = new JSONObject();
                        JSONObject item = new JSONObject();
                        item.put("name", foodName);
                        item.put("quantity", foodQuantityPicker.getValue());
                        entry.put(foodName , item);
                        js.put(timing, entry);
                    }

            }
            else{

                JSONObject entry = new JSONObject();
                JSONObject item = new JSONObject();
                item.put("name", foodName);
                item.put("quantity", foodQuantityPicker.getValue());
                entry.put(foodName , item);
                js.put(timing, entry);


            }

        } catch (Exception e) {
        }



        myEdit.putString(key, js.toString());
        myEdit.apply();


    }

}