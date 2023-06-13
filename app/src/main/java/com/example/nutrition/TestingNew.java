package com.example.nutrition;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class TestingNew extends AppCompatActivity {

    // creating variables for
    // our ui components.
    private RecyclerView foodRV;

    // variable for our adapter
    // class and array list
    private FoodAdapter adapter;
    private ArrayList<FoodModel> FoodModelArrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_new);

        // initializing our variables.
        foodRV = (RecyclerView) findViewById(R.id.idRVFood);

        // calling method to
        // build recycler view.

        buildRecyclerView();
    }

    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<FoodModel> filteredlist = new ArrayList<FoodModel>();

        // running a for loop to compare elements.
        for (FoodModel item : FoodModelArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getFoodName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void buildRecyclerView() {

        // below line we are creating a new array list
        FoodModelArrayList = new ArrayList<FoodModel>();

        // below line is to add data to our array list.
        try{

            InputStream iStream = getAssets().open("food.json");
            int size = iStream.available();
            byte[] buffer =  new byte[size];
            iStream.read(buffer);
            String  json = new String(buffer, "UTF-8");
            JSONObject jsonObj = new JSONObject(json);
            Iterator<String> keys = jsonObj.keys();
            while (keys.hasNext()){
                String key = keys.next();
                JSONObject item = (JSONObject) jsonObj.get(key);
                FoodModelArrayList.add(new FoodModel(key.toString()));
            }

        }catch (Exception e){
            Toast.makeText(this, "" + e , Toast.LENGTH_SHORT).show();
        }







        // initializing our adapter class.
        adapter = new FoodAdapter(FoodModelArrayList, TestingNew.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        foodRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        foodRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        foodRV.setAdapter(adapter);
    }




}
