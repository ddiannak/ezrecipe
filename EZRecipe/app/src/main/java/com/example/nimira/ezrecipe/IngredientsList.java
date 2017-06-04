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
}
