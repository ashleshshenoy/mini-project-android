package com.example.nutrition;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;

public  class Analytics extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] macros = {"Protien", "Fat", "Carbs", "Fibre"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);







        //barchat
        BarChart chart = (BarChart) findViewById(R.id.chart);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("feb 1", "feb 2","feb 3 ", "feb 4", "feb 5", "feb 6", "feb 7"));
        List<BarEntry> incomeEntries = getMacroEntries();
        dataSets = new ArrayList<>();
        BarDataSet set;
        set = new BarDataSet(incomeEntries, "macros");
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





        //pie chart
        PieChart pieChart = (PieChart)findViewById(R.id.pieChart);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, "Protien"));
        entries.add(new PieEntry(30.8f, "Fibre"));
        entries.add(new PieEntry(26.7f, "Fat"));
        entries.add(new PieEntry(50.0f, "Carbs"));
        PieDataSet pieSet = new PieDataSet(entries, "Macro composition");
        PieData pieData = new PieData(pieSet);
        pieSet.setColors(R.color.white);
        pieSet.setColors(new int[] { Color.rgb(225,20,0), Color.rgb(100,225,20),Color.rgb(225,105,0), Color.rgb(0,140,225) });

        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh










        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.macroSpinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,macros);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);





    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Toast.makeText(getApplicationContext(),macros[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private List<BarEntry> getMacroEntries() {
        ArrayList<BarEntry> macroEntries = new ArrayList<>();

        macroEntries.add(new BarEntry(0, 11300));
        macroEntries.add(new BarEntry(1, 1390));
        macroEntries.add(new BarEntry(2, 1190));
        macroEntries.add(new BarEntry(3, 7200));
        macroEntries.add(new BarEntry(4, 4790));
        macroEntries.add(new BarEntry(5, 4500));
        macroEntries.add(new BarEntry(6, 8000));


        return macroEntries;
    }


}