package com.example.nutrition;

public class FoodModel {
    // variables for our course
    // name and description.
    private String FoodName;

    // creating constructor for our variables.
    public FoodModel(String FoodName) {
        this.FoodName = FoodName;
    }

    // creating getter and setter methods.
    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String FoodName) {
        this.FoodName = FoodName;
    }

}
