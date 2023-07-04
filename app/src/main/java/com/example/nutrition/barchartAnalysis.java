package com.example.nutrition;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class barchartAnalysis extends Fragment  implements AdapterView.OnItemSelectedListener{

    String[] macros = {"calorie","protein", "fat", "carb", "fibre", "water"};

    String date;

    Double protein = 0.0;
    Double fat=0.0;
    Double fibre =0.0;
    Double carbs = 0.0;
    BarChart chart;
    TextView weeklyTotalMacros;
    TextView weeklyAverageMarcos;
    public barchartAnalysis() {
        super(R.layout.fragment_barchart_analysis);
    }




    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        date = requireArguments().getString("dateArgument");
        if (container != null) {
            container.removeAllViews();
        }

        View view =  inflater.inflate(R.layout.fragment_barchart_analysis, container, false);
        chart = (BarChart) view.findViewById(R.id.chart);
        weeklyAverageMarcos = (TextView) view.findViewById(R.id.weeklyAverageMarcos);
        weeklyTotalMacros = (TextView) view.findViewById(R.id.weeklyTotalMacros);




        drawChart(getMacroEntries("calorie"));




        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) view.findViewById(R.id.macroSpinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,macros);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if(position == 5)
            drawChart(getWaterLog());
        else
            drawChart(getMacroEntries(macros[position]));

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private void drawChart(ArrayList<BarEntry> barEntryData){
        //barchat
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        List<String> xAxisValues = getXAxis();
        List<BarEntry>  macroEntries = barEntryData;
        dataSets = new ArrayList<>();
        BarDataSet set;
        set = new BarDataSet(macroEntries, "macros");
        set.setColor(Color.rgb(255, 175, 0));
        set.setValueTextColor(R.color.black);
        dataSets.add(set);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setExtraLeftOffset(15);
        chart.setExtraRightOffset(15);
        //to hide background lines
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        //to hide right Y and top X border
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setEnabled(false);
        XAxis bottomXAxis = chart.getXAxis();
        bottomXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomXAxis.setEnabled(true);
        chart.getDescription().setEnabled(false);
        //data
        chart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        chart.setData(data);
        //animate & refresh
        chart.animateX(2000);
        chart.invalidate();
    }
    private ArrayList<String> getXAxis() {

        ArrayList<String> week = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat nd = new SimpleDateFormat("MMM dd");
        Date today;
        try {

            today = sd.parse(date);
            for(int i=0; i<7; i++){

                week.add(nd.format(today));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.add(Calendar.DATE, -1);
                today = calendar.getTime();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Collections.reverse(week);
        return week;
    }


    private ArrayList<BarEntry> getMacroEntries(String macro) {
        ArrayList<BarEntry> macroEntries = new ArrayList<>();


        Double weeklyTotalMacrosVolume =0.0;
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        String day = date;
        Date tempDay;

        try {
            for(int i=0; i<7; i++){

                Double totalMacros = 0.0;

                //opening food detail file
                InputStream iStream = getActivity().getAssets().open("food.json");
                int size = iStream.available();
                byte[] buffer =  new byte[size];
                iStream.read(buffer);
                String  json = new String(buffer, "UTF-8");
                JSONObject foodDetail = new JSONObject(json);
                SharedPreferences sp = getActivity().getSharedPreferences("FoodLog", MODE_PRIVATE);
                if(!sp.contains(day)) macroEntries.add(new BarEntry(6-i, 0)) ;
                else{
                    JSONObject dayLog = new JSONObject(sp.getString(day, ""));

                    if(dayLog.has(calorie.timing.BREAKFAST.toString())) {
                        JSONObject breakfast = dayLog.getJSONObject(calorie.timing.BREAKFAST.toString());
                        Iterator<String> keys = breakfast.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject item = breakfast.getJSONObject(key);
                            totalMacros +=  foodDetail.getJSONObject(key).getDouble(macro) * (int) item.get("quantity") ;
                        }
                    }
                    if(dayLog.has(calorie.timing.MORNING_SNACK.toString())) {
                        JSONObject morningSnack = dayLog.getJSONObject(calorie.timing.MORNING_SNACK.toString());
                        Iterator<String> keys = morningSnack.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject item = morningSnack.getJSONObject(key);
                            totalMacros +=  foodDetail.getJSONObject(key).getDouble(macro) * (int) item.get("quantity") ;
                        }
                    }
                    if(dayLog.has(calorie.timing.LUNCH.toString())) {
                        JSONObject lunch = dayLog.getJSONObject(calorie.timing.LUNCH.toString());
                        Iterator<String> keys = lunch.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject item = lunch.getJSONObject(key);
                            totalMacros +=  foodDetail.getJSONObject(key).getDouble(macro) * (int) item.get("quantity") ;
                        }
                    }
                    if(dayLog.has(calorie.timing.EVENING_SNACK.toString())) {
                        JSONObject eveningSnack = dayLog.getJSONObject(calorie.timing.EVENING_SNACK.toString());
                        Iterator<String> keys = eveningSnack.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject item = eveningSnack.getJSONObject(key);
                            totalMacros +=  foodDetail.getJSONObject(key).getDouble(macro) * (int) item.get("quantity") ;
                        }
                    }
                    if(dayLog.has(calorie.timing.DINNER.toString())) {
                        JSONObject dinner = dayLog.getJSONObject(calorie.timing.DINNER.toString());
                        Iterator<String> keys = dinner.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject item = dinner.getJSONObject(key);
                            totalMacros +=  foodDetail.getJSONObject(key).getDouble(macro) * (int) item.get("quantity") ;
                        }
                    }


                    weeklyTotalMacrosVolume += totalMacros;
                    //add to list
                    macroEntries.add(  new BarEntry(6-i, Math.round(totalMacros)));


                }
                tempDay = sd.parse(day);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tempDay);
                calendar.add(Calendar.DATE, -1);
                tempDay = calendar.getTime();
                day = sd.format(tempDay);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        weeklyTotalMacros.setText(String.format("%.0f", weeklyTotalMacrosVolume));
        weeklyAverageMarcos.setText(String.format("%.0f", weeklyTotalMacrosVolume/7));

        return  macroEntries ;
    }

    private ArrayList<BarEntry> getWaterLog() {

        int watertotal=0;
        ArrayList<BarEntry> waterEntries = new ArrayList<>();
        SharedPreferences waterSp = getActivity().getSharedPreferences("WaterLog", MODE_PRIVATE);

        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        String day = date;
        Date tempDay;
        try {
            for (int i = 0; i < 7; i++) {

                waterEntries.add(  new BarEntry(6-i, waterSp.getInt(day, 0)));
                watertotal += waterSp.getInt(day,0);
                tempDay = sd.parse(day);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tempDay);
                calendar.add(Calendar.DATE, -1);
                tempDay = calendar.getTime();
                day = sd.format(tempDay);
            }
        }catch (Exception e){

        }
        weeklyTotalMacros.setText(""+ watertotal);
        weeklyAverageMarcos.setText(String.format("%.1f",(double)watertotal/7.0));
        return waterEntries;
    }


}