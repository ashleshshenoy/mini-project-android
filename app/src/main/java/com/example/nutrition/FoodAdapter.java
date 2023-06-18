package com.example.nutrition;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    String date;
    String timing;
    // creating a variable for array list and context.
    private ArrayList<FoodModel> FoodModelArrayList;
    // creating a constructor for our variables.
    Context context;
    public FoodAdapter(ArrayList<FoodModel> FoodModelArrayList, Context context, String date, String timing) {
        this.FoodModelArrayList = FoodModelArrayList;
        this.context = context;
        this.date = date;
        this.timing = timing;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<FoodModel> filterlist) {
        // below line is to add our filtered
        // list in our Food array list.
        FoodModelArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_rv_item, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        FoodModel model = FoodModelArrayList.get(position);
        holder.FoodNameTV.setText(model.getFoodName());
    }


    @Override
    public int getItemCount() {
        // returning the size of array list.
        return FoodModelArrayList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // creating variables for our views.
        private final TextView FoodNameTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            // initializing our views with their ids.
            FoodNameTV = itemView.findViewById(R.id.idTVFoodName);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            FoodModel selectedFood = FoodModelArrayList.get(position);
            String name = selectedFood.getFoodName();


            Intent i = new Intent(context, FoodInput.class );
            i.putExtra("name", name);
            i.putExtra("date", date);
            i.putExtra("timing", timing);
            context.startActivity(i);

            Log.d("clickfromlistener", "clicked"+ position);
        }
    }
}
