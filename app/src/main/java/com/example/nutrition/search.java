package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Barrier;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class search extends AppCompatActivity {

    EditText searchBar;
    LinearLayout itemContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_search);


        searchBar = (EditText) findViewById(R.id.searchBar);
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        itemContainer = (LinearLayout) findViewById(R.id.itemContainer);

        listFoodItems();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            }
        });

    }

    public void listFoodItems(){
        try{

            InputStream iStream = getAssets().open("food.json");
            int size = iStream.available();
            byte[] buffer =  new byte[size];
            iStream.read(buffer);
            String  json = new String(buffer, "UTF-8");
            JSONObject jsonObj = new JSONObject(json);
            JSONArray items = jsonObj.getJSONArray("item");
            for(int i=0; i< items.length() ; i++){
                JSONObject item = (JSONObject) items.get(i);
                TextView txt = new TextView(search.this);
                txt.setText(item.getString("name"));
                txt.setElevation(20);
                txt.setHeight(100);
                txt.setGravity(Gravity.CENTER_VERTICAL);
                txt.setBackgroundColor((int)R.color.white);
                txt.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                ImageButton addBtn = new ImageButton(search.this);
                addBtn.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                addBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addBtn.setBackgroundColor((int)R.color.white);
                addBtn.setImageResource(R.drawable.plusicon);

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(search.this, FoodInput.class);
                        i.putExtra("name", txt.getText());
                        startActivity(i);
                    }
                });
                LinearLayout ll = new LinearLayout(search.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setBackgroundColor((int)R.color.purple_200);
                ll.addView(txt);
                ll.setPadding(10, 50, 10,50);
                ll.addView(addBtn);
                itemContainer.addView(ll);
            }

        }catch (Exception e){
            TextView txt = new TextView(search.this);
            txt.setText(e.toString());
            itemContainer.addView(txt);
        }

    }



}