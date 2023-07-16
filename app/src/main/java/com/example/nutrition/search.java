package com.example.nutrition;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Visibility;

import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class search extends AppCompatActivity {

;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(true);

        DayScrollDatePicker sd = (DayScrollDatePicker) findViewById(R.id.day_date_picker);
        sd.setStartDate(1,1,2023);
        sd.setEndDate(14,7,2023);
        sd.setVisibility(View.GONE);



        sd.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                SimpleDateFormat sd = new SimpleDateFormat("DD/MM/YYYY");

            }
        });

    }


}