package com.example.nutrition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Barrier;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
                txt.setPadding(40,40,40,40);
                txt.setElevation(20);
                ImageButton addBtn = new ImageButton(search.this);
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
                ll.addView(txt);
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