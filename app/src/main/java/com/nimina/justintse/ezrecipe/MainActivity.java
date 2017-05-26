package com.nimina.justintse.ezrecipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/479101/information?includeNutrition=false")
                    .header("X-Mashape-Key", "IBIjQLdnrhmsh19NxOPSpjaWSVIqp1owlcfjsnmDQyC8gFp1HD")
                    .header("Accept", "application/json")
                    .asJson();
            Log.i("jsonresponse: ", ""+response);
            Log.i("test", "what");
        }
        catch(UnirestException e) {
            Log.i("exception", ""+e);
        }
    }
}
