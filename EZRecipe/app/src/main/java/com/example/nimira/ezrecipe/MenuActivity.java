package com.example.nimira.ezrecipe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        //ArrayList<String> recipeIDs = (ArrayList<String>) intent.getSerializableExtra("recipeIDs");
        //ArrayList<String> recipeNames = (ArrayList<String>) intent.getSerializableExtra("recipeNames");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //sets value, adds list of new recipe searches to previous list.
        //Spinner recipes = (Spinner)findViewById(R.id.recipeDropdown);

        recipeIDs = (ArrayList<String>) intent.getSerializableExtra("recipeIDs");
        recipeNames = (ArrayList<String>) intent.getSerializableExtra("recipeNames");
        recipeImages = (ArrayList<String>) intent.getSerializableExtra("recipeImage");
        recipes = (Spinner)findViewById(R.id.recipeDropdown);
        ref.setValue(recipeIDs);
        ref.push().setValue(recipeIDs);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipeNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipes.setAdapter(dataAdapter);

        Button getRecipe = (Button)findViewById(R.id.getRecipe);
        image = (ImageView)findViewById(R.id.image);

        getRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                String getselected = recipes.getSelectedItem().toString();
                for (int i = 0;i<recipeNames.size();i++) {
                    if (recipeNames.get(i).matches(getselected)) {
                        new CallMashapeAsync().execute(recipeIDs.get(i));
                    }
                }
                } catch (Exception e) {
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
            String items = "";
            String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+msg[0]+"/information";
            Log.i("url: ", url);
            HttpResponse<JsonNode> request = null;
            try {
                request = Unirest.get(url)
                        .header("X-Mashape-Key", "gNrvLXTPTNmshsXWUXLzm7VwvkJWp1m47mVjsn5eRbKVitWD4i")
                        .header("Accept", "application/json")
                        .asJson();
                Log.i("request", "" + request);
            } catch (UnirestException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return request;
        }


        protected void onProgressUpdate(Integer...integers) {
        }

        protected void onPostExecute(HttpResponse<JsonNode> response) {
            // Parses json response for printing out the string of recipe id's and the response
            String answer = response.getBody().toString();
            //TextView infoview = (TextView)findViewById(R.id.infoview);
            // infoview.setText(answer);
            try {
                JSONObject root = new JSONObject(answer);
                // JSONArray recipe_name = root.getJSONArray(0);
                //  JSONObject id_num = root.getJSONObject("sourceUrl");
                String idstr = root.getString("sourceUrl");
               // infoview.setText(idstr);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(idstr));
                startActivity(i);
            }
            catch (JSONException e) {
                Log.i("failed:", " make sure you are getting the right type");
              //  infoview.setText(answer);
            }
        }
    }

}


