package com.example.nutrition;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class calorie extends AppCompatActivity {

    String date;

    enum timing {
        BREAKFAST,
        MORNING_SNACK,
        LUNCH,
        EVENING_SNACK,
        DINNER
    }


    ImageView analyticsBtn;
    ImageView breakfastAddBtn;
    ImageView morningSnackAddBtn;
    ImageView lunchAddBtn;
    ImageView eveningSnackAddBtn;
    ImageView dinnerAddBtn;



    TextView breakfastHelpText;
    String breakfastHelpTextString = "All you need is some breakfast .  \uD83C\uDFC3☀️";

    TextView morningSnackHelpText;
    String morningSnackHelpTextString = "Get Energized by grabbing a morning snack.  \uD83E\uDD5C";
    TextView lunchHelpText;
    String lunchHelpTextString = "Don't miss lunch \uD83C\uDF71 its time to get a tasty meal.";
    TextView eveningSnackHelpText;
    String eveningSnackHelpTextString = "Refresh yourself with a delicious evening snack \uD83E\uDD51.";
    TextView dinnerHelpText;
    String dinnerHelpTextString = "An early dinner can help you sleep better \uD83C\uDF7D️\uD83D\uDE34";

    TextView totalBreakfastCalorieHolder;
    TextView totalMorningSnackCalorieHolder;
    TextView totalLunchCalorieHolder;
    TextView totalEveningSnackCalorieHolder;
    TextView totalDinnerCalorieHolder;

    TextView totalDailyCalorieHolder;

    LinearLayout breakfastContainer;
    LinearLayout morningSnackContainer;
    LinearLayout lunchContainer;
    LinearLayout eveningSnackContainer;
    LinearLayout dinnerContainer;


    SharedPreferences sp;
    SharedPreferences.Editor myEdit;
    JSONObject foodDetail;
    int totalBreakfastCalorie = 0;
    int totalMorningSnackCalorie = 0;
    int totalLunchCalorie = 0;
    int totalEveningSnackCalorie = 0;
    int totalDinnerCalorie = 0;
    int totalDailyCalorie = 0;

    int dailyCalorieBuget;


    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);
        // showing the back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setCustomView(R.layout.actionbar_custom_view_home);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);





       // set date at header
        SimpleDateFormat fdate = new SimpleDateFormat("dd/MM/yyyy");
        TextView titleText = (TextView) findViewById(R.id.titleDateHolder);
        date = (getIntent().hasExtra("date"))? getIntent().getStringExtra("date"): fdate.format(new Date());
        try {

            if(date.contentEquals(fdate.format(new Date())))
                titleText.setText("Today");
            else
                titleText.setText(new SimpleDateFormat("dd MMM").format(fdate.parse(date)));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        //initalise everything
        analyticsBtn = (ImageView) findViewById(R.id.analyticsBtn);
        breakfastAddBtn = (ImageView) findViewById(R.id.breakfastAddBtn);
        morningSnackAddBtn = (ImageView) findViewById(R.id.morningSnackAddBtn);
        lunchAddBtn  = (ImageView) findViewById(R.id.lunchAddBtn);
        eveningSnackAddBtn = (ImageView) findViewById(R.id.eveningSnackAddBtn);
        dinnerAddBtn = (ImageView)findViewById(R.id.dinnerAddBtn);
        breakfastContainer = (LinearLayout) findViewById(R.id.breakfastContainer);
        morningSnackContainer = (LinearLayout) findViewById(R.id.morningSnackContainer);
        lunchContainer = (LinearLayout) findViewById(R.id.lunchContainer);
        eveningSnackContainer  = (LinearLayout) findViewById(R.id.eveningSnackContainer);
        dinnerContainer = (LinearLayout) findViewById(R.id.dinnerContainer);
        breakfastHelpText = (TextView) findViewById(R.id.breakFastHelpText);
        morningSnackHelpText  = (TextView)findViewById(R.id.morningSnackHelpText);
        lunchHelpText = (TextView)findViewById(R.id.lunchHelpText);
        eveningSnackHelpText = (TextView)findViewById(R.id.eveningSnackHelpText);
        dinnerHelpText = (TextView) findViewById(R.id.dinnerHelpText);
        totalBreakfastCalorieHolder = (TextView) findViewById(R.id.totalBreakfastCalorieHolder);
        totalMorningSnackCalorieHolder = (TextView) findViewById(R.id.totalMorningSnackCalorieHolder);
        totalLunchCalorieHolder  = (TextView) findViewById(R.id.totalLunchCalorieHolder);
        totalEveningSnackCalorieHolder = (TextView) findViewById(R.id.totalEveningCalorieHolder);
        totalDinnerCalorieHolder = (TextView) findViewById(R.id.totalDinnerCalorieHolder);
        totalDailyCalorieHolder = (TextView) findViewById(R.id.totalDailyCalorieHolder);


        // setting daily calories buget;
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        dailyCalorieBuget =  sharedPreferences.getInt("calorie", 0);
        totalDailyCalorieHolder.setText("0 of " + dailyCalorieBuget + " calories");
        totalBreakfastCalorieHolder.setText("0 of " + (int)Math.round(dailyCalorieBuget * 0.25) + " cal");
        totalMorningSnackCalorieHolder.setText("0 of " +(int)Math.round( dailyCalorieBuget * 0.125) + " cal");
        totalLunchCalorieHolder.setText("0 of " + (int)Math.round(dailyCalorieBuget * 0.25) + " cal");
        totalEveningSnackCalorieHolder.setText("0 of " + (int)Math.round(dailyCalorieBuget * 0.125) + " cal");
        totalDinnerCalorieHolder.setText("0 of "+ (int)Math.round(dailyCalorieBuget * 0.25) + " cal");




        DayScrollDatePicker sd = (DayScrollDatePicker) findViewById(R.id.day_date_picker);
        LocalDate today = LocalDate.now();
        sd.setEndDate(today.getDayOfMonth(), today.getMonthOfYear(), today.getYear());
        LocalDate lastAvailableDate = today.minusDays(15);
        sd.setStartDate(lastAvailableDate.getDayOfMonth(), lastAvailableDate.getMonthOfYear(), lastAvailableDate.getYear());
        sd.setVisibility(View.GONE);


        actionBar.getCustomView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sd.getVisibility() == View.VISIBLE)
                    sd.setVisibility(View.GONE);
                else {
                    sd.setVisibility(View.VISIBLE);
                }
            }
        });



        sd.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                Toast.makeText(calorie.this, "" + fdate.format(date), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(calorie.this, calorie.class);
                i.putExtra("date", fdate.format(date));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });




        //calling food lob data for the date
        loadData();
        totalDailyCalorie = totalBreakfastCalorie + totalMorningSnackCalorie + totalLunchCalorie + totalEveningSnackCalorie + totalDinnerCalorie;
        String[] totalDailyCalorieText = totalDailyCalorieHolder.getText().toString().trim().split(" ");
        totalDailyCalorieText[0] = String.valueOf(totalDailyCalorie);
        totalDailyCalorieHolder.setText(String.join(" ", totalDailyCalorieText));






        analyticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(calorie.this, Analytics.class);
                i.putExtra("date", date);
                startActivity(i);
            }
        });


        breakfastAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(calorie.this, TestingNew.class);
                i.putExtra ("timing", timing.BREAKFAST.toString());
                i.putExtra("date", date);
                startActivity(i);

            }
        });
        morningSnackAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(calorie.this, TestingNew.class);
                i.putExtra ("timing", timing.MORNING_SNACK.toString());
                i.putExtra("date", date);
                startActivity(i);

            }
        });

        lunchAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(calorie.this, TestingNew.class);
                i.putExtra ("timing", timing.LUNCH.toString());
                i.putExtra("date", date);
                startActivity(i);

            }
        });

        eveningSnackAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(calorie.this, TestingNew.class);
                i.putExtra ("timing", timing.EVENING_SNACK.toString());
                i.putExtra("date", date);
                startActivity(i);

            }
        });

        dinnerAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(calorie.this, TestingNew.class);
                i.putExtra ("timing", timing.DINNER.toString());
                i.putExtra("date", date);
                startActivity(i);

            }
        });





    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void loadData(){

        try {


            //open food date file
            InputStream iStream = getAssets().open("food.json");
            int size = iStream.available();
            byte[] buffer =  new byte[size];
            iStream.read(buffer);
            String  json = new String(buffer, "UTF-8");
            foodDetail = new JSONObject(json);




            // saved user data
            sp = getSharedPreferences("FoodLog", MODE_PRIVATE);
            myEdit = sp.edit();
            if(!sp.contains(date)) return;
            JSONObject js = new JSONObject(sp.getString(date,"eroor"));



            //breakfast data
            if(js.has(timing.BREAKFAST.toString())){
                generateFoodList(js, timing.BREAKFAST.toString(), breakfastContainer, breakfastHelpText);
            }


            //morning snack data
            if(js.has(timing.MORNING_SNACK.toString())){
                generateFoodList(js, timing.MORNING_SNACK.toString(), morningSnackContainer, morningSnackHelpText);
            }

            // lunch data
            if(js.has(timing.LUNCH.toString())){
                generateFoodList(js, timing.LUNCH.toString(), lunchContainer, lunchHelpText);
            }

            // evening snack data
            if(js.has(timing.EVENING_SNACK.toString())){
                generateFoodList(js, timing.EVENING_SNACK.toString(), eveningSnackContainer, eveningSnackHelpText);
            }

            //dinner data
            if(js.has(timing.DINNER.toString())){
                generateFoodList(js, timing.DINNER.toString(), dinnerContainer, dinnerHelpText);
            }

        }catch (Exception e){
            Log.d("Error" , e + "");
        }
    }


    public void generateFoodList(JSONObject js, String timing, LinearLayout container, TextView hintTextHolder) throws JSONException {
        JSONObject jso = js.getJSONObject(timing);
        Iterator<String> keys = jso.keys();
        if(keys.hasNext()) container.removeView(hintTextHolder);
        while(keys.hasNext()){
            String key = keys.next();
            JSONObject item = jso.getJSONObject(key);
            TextView itemName = new TextView(calorie.this);
            itemName.setText(item.get("name").toString());
            itemName.setHeight(100);
            itemName.setGravity(Gravity.CENTER_VERTICAL);
            TextView itemQuantity = new TextView(calorie.this);
            itemQuantity.setText(item.get("quantity").toString() + " serve");
            itemQuantity.setHeight(100);
            itemQuantity.setGravity(Gravity.CENTER_VERTICAL);
            ImageButton imgButton = new ImageButton(calorie.this);
            imgButton.setImageResource(R.drawable.option);
            imgButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            imgButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgButton.setBackgroundColor(00000);
            imgButton.setForegroundGravity(Gravity.CENTER_VERTICAL);
            LinearLayout ll = new LinearLayout(calorie.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.addView(itemName);
            ll.addView(itemQuantity);
            ll.addView(imgButton);
            ll.setPadding(20,50,20,50);
            itemName.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            container.addView(ll);




            //for deleting
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout linLayout  = (LinearLayout) imgButton.getParent();
                    TextView txt = (TextView) linLayout.getChildAt(0);
                    jso.remove(txt.getText().toString());
                    try {
                        js.put(timing,jso);
                        myEdit.putString(date,js.toString());
                        myEdit.commit();
                        recreate();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });



            switch (timing){
                case "BREAKFAST":
                    //counting total breakfast calorie
                    totalBreakfastCalorie +=  foodDetail.getJSONObject(key).getInt("calorie") * (int) item.get("quantity") ;
                    String[] totalBreakfastCalorieText = totalBreakfastCalorieHolder.getText().toString().trim().split(" ");
                    totalBreakfastCalorieText[0] = String.valueOf(totalBreakfastCalorie);
                    totalBreakfastCalorieHolder.setText(String.join(" ", totalBreakfastCalorieText) );
                    break;

                case "MORNING_SNACK":
                    //counting total morning snack calorie
                    totalMorningSnackCalorie +=  foodDetail.getJSONObject(key).getInt("calorie") * (int) item.get("quantity") ;
                    String[] totalMorningSnackCalorieText = totalMorningSnackCalorieHolder.getText().toString().trim().split(" ");
                    totalMorningSnackCalorieText[0] = String.valueOf(totalMorningSnackCalorie);
                    totalMorningSnackCalorieHolder.setText(String.join(" ", totalMorningSnackCalorieText) );
                    break;

                case "LUNCH":
                    //counting total lunch calorie
                    totalLunchCalorie +=  foodDetail.getJSONObject(key).getInt("calorie") * (int) item.get("quantity") ;
                    String[] totalLunchCalorieText = totalLunchCalorieHolder.getText().toString().trim().split(" ");
                    totalLunchCalorieText[0] = String.valueOf(totalLunchCalorie);
                    totalLunchCalorieHolder.setText(String.join(" ", totalLunchCalorieText));
                    break;

                case "EVENING_SNACK":
                    //total evening calorie counting
                    totalEveningSnackCalorie +=  foodDetail.getJSONObject(key).getInt("calorie") * (int) item.get("quantity") ;
                    String[] totalEveningSnackCalorieText = totalEveningSnackCalorieHolder.getText().toString().trim().split(" ");
                    totalEveningSnackCalorieText[0] = String.valueOf(totalEveningSnackCalorie);
                    totalEveningSnackCalorieHolder.setText(String.join(" ", totalEveningSnackCalorieText));
                    break;

                case "DINNER":
                    //total Dinner counting
                    totalDinnerCalorie +=  foodDetail.getJSONObject(key).getInt("calorie") * (int) item.get("quantity") ;
                    String[] totalDinnerCalorieText  = totalDinnerCalorieHolder.getText().toString().trim().split(" ");
                    totalDinnerCalorieText[0] = String.valueOf(totalDinnerCalorie);
                    totalDinnerCalorieHolder.setText(String.join(" ", totalDinnerCalorieText));
                    break;

            }


        }


    }





}