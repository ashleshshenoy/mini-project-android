package com.example.nutrition;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

public  class Analytics extends AppCompatActivity {

    String date ="today";

    Double protein = 0.0;
    Double fat = 0.0;
    Double fibre = 0.0;
    Double carbs = 0.0;
    int calories =0;

    TextView proteinProgressText;
    TextView fatProgressText;
    TextView fibreProgressText;
    TextView carbsProgressText;
    TextView calorieProgressText;
    TextView calorieProgressPercentage;
    TextView proteinProgressPercentage;
    TextView carbsProgressPercentage;
    TextView fibreProgressPercentage;
    TextView fatProgressPercentage;


    ProgressBar proteinProgressBar;
    ProgressBar fatProgressBar;
    ProgressBar fibreProgressBar;
    ProgressBar carbsProgressBar;



    public Analytics() {
        super(R.layout.activity_analytics);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            date = getIntent().getStringExtra("date");
            bundle.putString("dateArgument", date);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.barchartContainer, barchartAnalysis.class, bundle)
                    .commit();
        }


        proteinProgressBar = (ProgressBar) findViewById(R.id.proteinProgressBar);
        fatProgressBar = (ProgressBar)findViewById(R.id.fatProgressBar);
        fibreProgressBar = (ProgressBar) findViewById(R.id.fibreProgressBar);
        carbsProgressBar = (ProgressBar)findViewById(R.id.carbsProgressBar);
        proteinProgressText = (TextView)findViewById(R.id.proteinProgressText);
        fatProgressText = (TextView)findViewById(R.id.fatProgressText);
        fibreProgressText = (TextView)findViewById(R.id.fibreProgressText);
        carbsProgressText = (TextView)findViewById(R.id.carbProgressText);
        calorieProgressText = (TextView)findViewById(R.id.calorieProgressText);
        proteinProgressPercentage = (TextView)findViewById(R.id.proteinProgressPercentage);
        calorieProgressPercentage = (TextView)findViewById(R.id.calorieProgressPercentage);
        fibreProgressPercentage = (TextView)findViewById(R.id.fibreProgressPercentage);
        carbsProgressPercentage = (TextView)findViewById(R.id.carbsProgressPercentage);
        fatProgressPercentage = (TextView)findViewById(R.id.fatProgressPercentage);


        loadMacrosData();
        loadMacroProgress();



        //pie chart
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry( Float.parseFloat(String.format("%.2f",protein)), "Protien"));
        entries.add(new PieEntry(Float.parseFloat(String.format("%.2f",fibre)), "Fibre"));
        entries.add(new PieEntry(Float.parseFloat(String.format("%.2f",fat)), "Fat"));
        entries.add(new PieEntry(Float.parseFloat(String.format("%.2f",carbs)), "Carbs"));
        PieDataSet pieSet = new PieDataSet(entries, "Macro composition");
        PieData pieData = new PieData(pieSet);
        pieSet.setColors(R.color.white);
        pieSet.setColors(new int[] { Color.rgb(225,20,0), Color.rgb(100,225,20),Color.rgb(225,105,0), Color.rgb(0,140,225) });
        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh



//        FragmentManager fragmentManager = getSupportFragmentManager();
//        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.piechartFragment, barchartAnalysis.getInstance(date)).commit();



    }



    public void loadMacroProgress(){

        int dProtein, dFat, dCarbs, dFibre, dCalorie;
        int proteinPercent , fatPercent, carbsPercent, fibrePercent, caloriePercent;

        SharedPreferences sp = getSharedPreferences("UserDetails", MODE_PRIVATE);
        dCalorie = sp.getInt("calorie", 0);
        dProtein = (int) Double.parseDouble(sp.getString("protein",""));
        dCarbs = (int) Double.parseDouble(sp.getString("carbs", ""));
        dFibre = (int) Double.parseDouble(sp.getString("fibre", ""));
        dFat = (int) Double.parseDouble(sp.getString("fat", ""));


        calorieProgressText.setText(calories + " of " + dCalorie + " cals");
        proteinProgressText.setText((int)Math.round(protein) + "/" + dProtein + "g" );
        fatProgressText.setText((int)Math.round(fat) + "/" + dFat + "g");
        fibreProgressText.setText((int)Math.round(fibre) + "/" + dFibre + "g");
        carbsProgressText.setText((int)Math.round(carbs) + "/"+ dCarbs + "g");

        caloriePercent =  (int) (Double.parseDouble(calories+"") / dCalorie * 100);
        proteinPercent = (int) (protein/dProtein* 100);
        fatPercent = (int)(fat/dFat * 100);
        fibrePercent = (int)(fibre/dFibre * 100);
        carbsPercent = (int)(carbs/dCarbs* 100);


        calorieProgressPercentage.setText(caloriePercent+ "%");
        proteinProgressPercentage.setText(proteinPercent + "%");
        fatProgressPercentage.setText( fatPercent + "%");
        fibreProgressPercentage.setText(fibrePercent + "%");
        carbsProgressPercentage.setText(carbsPercent + "%");




        proteinProgressBar.setProgress(proteinPercent, true);
        fatProgressBar.setProgress(fatPercent, true);
        fibreProgressBar.setProgress(fibrePercent, true);
        carbsProgressBar.setProgress(carbsPercent, true);



        //setting color to progressbar depending upon its progress
        proteinProgressBar.setProgressTintList(getProgressColor(proteinProgressBar.getProgress()));
        fatProgressBar.setProgressTintList(getProgressColor(fatProgressBar.getProgress()));
        fibreProgressBar.setProgressTintList(getProgressColor(fibreProgressBar.getProgress()));
        carbsProgressBar.setProgressTintList(getProgressColor(carbsProgressBar.getProgress()));



    }

    public ColorStateList getProgressColor(int progress){
        if(progress > 120)
            return ColorStateList.valueOf(Color.rgb(255, 68, 51));
        else if(progress > 90)
            return ColorStateList.valueOf(Color.rgb(236, 88, 0));
        else if(progress > 80)
            return ColorStateList.valueOf(Color.rgb(0, 163, 108));
        else if(progress > 40)
            return ColorStateList.valueOf(Color.rgb(255, 191, 0));
        else
            return ColorStateList.valueOf(Color.rgb(253, 218, 13    ));
    }


    public void loadMacrosData(){

        JSONObject foodDetail;
        try {


            //opening food detail file
            InputStream iStream = getAssets().open("food.json");
            int size = iStream.available();
            byte[] buffer =  new byte[size];
            iStream.read(buffer);
            String  json = new String(buffer, "UTF-8");
            foodDetail = new JSONObject(json);

            SharedPreferences sp = getSharedPreferences("FoodLog", MODE_PRIVATE);
            if(!sp.contains(date)) return ;

            JSONObject dayLog = new JSONObject(sp.getString(date, ""));


            if(dayLog.has(calorie.timing.BREAKFAST.toString())) {

                JSONObject breakfast = dayLog.getJSONObject(calorie.timing.BREAKFAST.toString());
                Iterator<String> keys = breakfast.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject item = breakfast.getJSONObject(key);
                    calories +=  foodDetail.getJSONObject(key).getDouble("calorie") * (int) item.get("quantity") ;
                    protein +=  foodDetail.getJSONObject(key).getDouble("protein") * (int) item.get("quantity") ;
                    fat +=  foodDetail.getJSONObject(key).getDouble("fat") * (int) item.get("quantity") ;
                    fibre +=  foodDetail.getJSONObject(key).getDouble("fibre") * (int) item.get("quantity") ;
                    carbs +=  foodDetail.getJSONObject(key).getDouble("carb") * (int) item.get("quantity") ;
                }



            }

            if(dayLog.has(calorie.timing.MORNING_SNACK.toString())){

                JSONObject morningSnack = dayLog.getJSONObject(calorie.timing.MORNING_SNACK.toString());
                Iterator<String> keys = morningSnack.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject item = morningSnack.getJSONObject(key);
                    calories +=  foodDetail.getJSONObject(key).getDouble("calorie") * (int) item.get("quantity") ;
                    protein +=  foodDetail.getJSONObject(key).getDouble("protein") * (int) item.get("quantity") ;
                    fat +=  foodDetail.getJSONObject(key).getDouble("fat") * (int) item.get("quantity") ;
                    fibre +=  foodDetail.getJSONObject(key).getDouble("fibre") * (int) item.get("quantity") ;
                    carbs +=  foodDetail.getJSONObject(key).getDouble("carb") * (int) item.get("quantity") ;
                }

            }
            if(dayLog.has(calorie.timing.LUNCH.toString())){

                JSONObject lunch = dayLog.getJSONObject(calorie.timing.LUNCH.toString());
                Iterator<String> keys = lunch.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject item = lunch.getJSONObject(key);
                    calories +=  foodDetail.getJSONObject(key).getDouble("calorie") * (int) item.get("quantity") ;
                    protein +=  foodDetail.getJSONObject(key).getDouble("protein") * (int) item.get("quantity") ;
                    fat +=  foodDetail.getJSONObject(key).getDouble("fat") * (int) item.get("quantity") ;
                    fibre +=  foodDetail.getJSONObject(key).getDouble("fibre") * (int) item.get("quantity") ;
                    carbs +=  foodDetail.getJSONObject(key).getDouble("carb") * (int) item.get("quantity") ;
                }

            }
            if(dayLog.has(calorie.timing.EVENING_SNACK.toString())){

                JSONObject eveningSnack = dayLog.getJSONObject(calorie.timing.EVENING_SNACK.toString());
                Iterator<String> keys = eveningSnack.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject item = eveningSnack.getJSONObject(key);
                    calories +=  foodDetail.getJSONObject(key).getDouble("calorie") * (int) item.get("quantity") ;
                    protein +=  foodDetail.getJSONObject(key).getDouble("protein") * (int) item.get("quantity") ;
                    fat +=  foodDetail.getJSONObject(key).getDouble("fat") * (int) item.get("quantity") ;
                    fibre +=  foodDetail.getJSONObject(key).getDouble("fibre") * (int) item.get("quantity") ;
                    carbs +=  foodDetail.getJSONObject(key).getDouble("carb") * (int) item.get("quantity") ;

                }
            }
            if(dayLog.has(calorie.timing.DINNER.toString())){
                JSONObject dinner = dayLog.getJSONObject(calorie.timing.DINNER.toString());
                Iterator<String> keys = dinner.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject item = dinner.getJSONObject(key);
                    calories +=  foodDetail.getJSONObject(key).getDouble("calorie") * (int) item.get("quantity") ;
                    protein +=  foodDetail.getJSONObject(key).getDouble("protein") * (int) item.get("quantity") ;
                    fat +=  foodDetail.getJSONObject(key).getDouble("fat") * (int) item.get("quantity") ;
                    fibre +=  foodDetail.getJSONObject(key).getDouble("fibre") * (int) item.get("quantity") ;
                    carbs +=  foodDetail.getJSONObject(key).getDouble("carb") * (int) item.get("quantity") ;
                }

            }

        }catch (Exception e){

        }



    }



}