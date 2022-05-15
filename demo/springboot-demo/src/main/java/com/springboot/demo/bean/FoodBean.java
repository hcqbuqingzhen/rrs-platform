package com.springboot.demo.bean;

public class FoodBean implements Mybean{
    private String foodName;

    public FoodBean(String foodName) {
        this.foodName = foodName;
    }
    public FoodBean() {

    }
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @Override
    public String toString() {
        return "FoodBean{" +
                "foodName='" + foodName + '\'' +
                '}';
    }
}
