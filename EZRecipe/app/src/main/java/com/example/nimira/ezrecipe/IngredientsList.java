package com.example.nimira.ezrecipe;

import java.util.ArrayList;

/**
 * Created by Brian on 6/3/17.
 */

public class IngredientsList {

    public ArrayList<String> ingredients;
    public String email;
    public String userId;

    public IngredientsList(){

    }

    public IngredientsList(ArrayList<String> ingredients, String userId, String email){
        this.ingredients = ingredients;
        this.userId = userId;
        this.email = email;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
