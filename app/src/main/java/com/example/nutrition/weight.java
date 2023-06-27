package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class weight extends AppCompatActivity {

    ImageButton editGoalButton;
    TextView weightProgressText;
    TextView durationLeftText;
    CircularProgressIndicator weightProgressIndicator;
    LineChart weightLineChart;
    LinearLayout weightLogContainer;
    SharedPreferences sh ;
    FloatingActionButton addWeightLog;
    SharedPreferences s;
    List<String> xAxisValues;

    List<Entry> entries;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        editGoalButton = (ImageButton) findViewById(R.id.editGoalButton);
        weightLineChart = (LineChart) findViewById(R.id.weightLineChart);
        weightProgressIndicator = (CircularProgressIndicator) findViewById(R.id.weightProgressIndicator);
        durationLeftText = (TextView) findViewById(R.id.durationLeftText);
        weightProgressText = findViewById(R.id.weightProgressText);
        weightLogContainer = findViewById(R.id.weightLogContainer);
        addWeightLog = findViewById(R.id.addWeightLogBtn);
        sh = getSharedPreferences("UserDetails", MODE_PRIVATE);
        s = getSharedPreferences("weightLog", MODE_PRIVATE);
        xAxisValues = new ArrayList<>();
        entries = new ArrayList<Entry>();

        loadData();
        loadWeightlog();



        List<Entry> val = entries;
        LineDataSet dataSet = new LineDataSet(val, "weight (in kg)");
        dataSet.setCircleColor(Color.parseColor("purple"));
        dataSet.setFillColor(Color.parseColor("purple"));
        dataSet.setLineWidth(3);
        dataSet.setColor(R.color.purple_700);



        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet);
        weightLineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
        LineData data = new LineData(dataSets);

        weightLineChart.setScaleEnabled(false);
        weightLineChart.setDrawGridBackground(false);
        weightLineChart.setExtraLeftOffset(15);
        weightLineChart.setExtraRightOffset(15);
        weightLineChart.getXAxis().setDrawGridLines(false);
        weightLineChart.getAxisLeft().setDrawGridLines(false);
        weightLineChart.getAxisRight().setDrawGridLines(false);
        YAxis rightYAxis = weightLineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis leftYAxis = weightLineChart.getAxisLeft();
        leftYAxis.setEnabled(true);

        XAxis bottomXAxis = weightLineChart.getXAxis();
        bottomXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomXAxis.setGranularity(1f);
        bottomXAxis.setGranularityEnabled(true);

        bottomXAxis.setEnabled(true);
        weightLineChart.getDescription().setEnabled(false);
        weightLineChart.setData(data);
        weightLineChart.invalidate();
        weightLineChart.setVisibleXRangeMaximum(4);
        weightLineChart.invalidate();



        editGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(weight.this, Goal.class);

                i.putExtra("bmi",  sh.getString("bmi",""));
                i.putExtra("height", sh.getFloat("height" , 0)+ "");
                i.putExtra("weight", sh.getFloat("weight", 0) + "");

                startActivity(i);
            }
        });

        addWeightLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(weight.this, AddWeightLog.class);
                startActivity(i);
            }
        });


    }

    public void loadData(){
        int week = Integer.parseInt(sh.getString("timeperiod", ""));
        Date startDate;
        startDate = new Date(sh.getString("startdate", ""));
        Double weight = (double) sh.getFloat("weight", 0);

        Toast.makeText(this, "" + weight, Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.WEEK_OF_YEAR,  week);
        Date endDate = calendar.getTime();

        long diffInMillies = endDate.getTime() - new Date().getTime() ;
        int weeksLeft = (int) Math.round(TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS)/7.0);
        if(weeksLeft < 0) weeksLeft = 0;
        durationLeftText.setText(weeksLeft + " week left.");

        int targetWeight = sh.getInt("targetWeight" , 0);
        int goal = sh.getInt("goal",0);
        double progress = 0;
        if(goal > 0 ){
             progress = (targetWeight + goal)  - weight;
            if(progress < 0 ) progress = 0;

        }
        else if(goal < 0){
             progress = weight - (targetWeight + goal) ;
            if(progress < 0 ) progress = 0;
        }else{
            Toast.makeText(this, "set goal", Toast.LENGTH_SHORT).show();
        }

        weightProgressIndicator.setProgress((int)((progress/goal) * 100), true);
        if(progress < goal)
            weightProgressText.setText(progress + " of "+ goal + " kg");
        else
            weightProgressText.setText("Set new goal \uD83C\uDFAF");


    }


    void loadWeightlog(){
        String weightLog = s.getString("log", "");
        try {
            if (weightLog.contentEquals("")) {
                TextView txt = new TextView(weight.this);
                txt.setText("none");
                weightLogContainer.addView(txt);
            } else {
                JSONArray weightLogObj = new JSONArray(weightLog);
                for (int i = 0 ; i < weightLogObj.length(); i++) {

                    // data for chart
                    JSONObject jsObj = (JSONObject) weightLogObj.get(i);
                    xAxisValues.add(jsObj.getString("date"));
                    entries.add(new Entry(  i , ((float) Double.parseDouble(jsObj.getString("weight")))));


                    // log tiles
                    LinearLayout ll = new LinearLayout(weight.this);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setPadding(50,50,50,50);
                    ll.setElevation(10);
                    ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    TextView dateTxt = new TextView(weight.this);
                    TextView weightTxt = new TextView(weight.this);
                    dateTxt.setText(jsObj.getString("date"));
                    dateTxt.setTextSize(12);
                    dateTxt.setTypeface(Typeface.DEFAULT_BOLD);
                    dateTxt.setTextColor((int)R.color.grey);
                    weightTxt.setText(jsObj.getString("weight") + " kg");
                    weightTxt.setTextColor(Color.parseColor("#6200EE"));
                    weightTxt.setPadding(0,0,0,20);
                    weightTxt.setTextSize(20);
                    weightTxt.setTypeface(Typeface.DEFAULT_BOLD);

                    ll.addView(weightTxt);
                    ll.addView(dateTxt);
                    int finalI = i;
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it = new Intent(weight.this, AddWeightLog.class);
                            it.putExtra("index", finalI);
                            try {
                                it.putExtra("weight", jsObj.getString("weight"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            startActivity(it);
                        }
                    });

                    weightLogContainer.addView(ll, 0);

                }

            }
        }
        catch (Exception e){

        }


    }




}