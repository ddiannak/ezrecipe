package com.example.nimira.ezrecipe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by miggycalleja on 5/15/17.
 */

public class MenuActivity extends Activity {
    ArrayList<String> recipeIDs;
    ArrayList<String> recipeNames;
    ArrayList<String> recipeImages;
    ImageView image;
    Spinner recipes;
    String text;
    int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        recipeIDs = (ArrayList<String>) intent.getSerializableExtra("recipeIDs");
        recipeNames = (ArrayList<String>) intent.getSerializableExtra("recipeNames");
        recipeImages = (ArrayList<String>) intent.getSerializableExtra("recipeImage");
        recipes = (Spinner)findViewById(R.id.recipeDropdown);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipeNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipes.setAdapter(dataAdapter);

        Button getRecipe = (Button)findViewById(R.id.getRecipe);
        image = (ImageView)findViewById(R.id.image);

        getRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                text = recipes.getSelectedItem().toString();
//                position=recipes.getSelectedItemPosition();
                Log.i("position1", ""+position);
                try {
                    HttpResponse<JsonNode> response = new CallMashapeAsync().execute("").get();
                    String data = response.getBody().toString();
                    Log.i("data", data);
//                    Picasso.with(getApplicationContext()).load(recipeImages.get(position)).into(image);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        recipes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Picasso.with(getApplicationContext()).load(recipeImages.get(position)).into(image);
                Log.i("selected item", (String) parentView.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {
//            String items = "";
//            for (int i=0; i<selection.size(); i++){
//                items = items + "" + selection.get(i) + "%2C";
//            }
            String recipeID = recipeIDs.get(position);
            Log.i("position2", ""+position);
            Log.i("recipeID", recipeID);
            String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + recipeID + "/information?includeNutrition=false";
//            Log.i("url: ", url);
            HttpResponse<JsonNode> request = null;
            try {

                request = Unirest.get(url)
                        .header("X-Mashape-Key", "gNrvLXTPTNmshsXWUXLzm7VwvkJWp1m47mVjsn5eRbKVitWD4i")
                        .header("Accept", "application/json")
                        .asJson();
                Log.i("request", "" + request);
            } catch (UnirestException e) {

                e.printStackTrace();
            }
//
            return request;
        }


        protected void onProgressUpdate(Integer...integers) {
        }

        protected void onPostExecute(HttpResponse<JsonNode> response) {
//            String answer = response.getBody().toString();
////            TextView txtView = (TextView) findViewById(R.id.textView1);
//            try {
//                // JSONObject root = new JSONObject(answer);
//                JSONArray root = new JSONArray(answer);
//                // JSONArray recipe_name = root.getJSONArray(0);
//                JSONObject id_num = root.getJSONObject(0);
//                int id = id_num.getInt("id");
//                String idstr = id_num.getString("id");
//                text.setText(idstr);
//            }
//            catch (JSONException e) {
//                text.setText("failed: make sure you are getting the right type");
////                text.setText(answer);
        }
    }

}


