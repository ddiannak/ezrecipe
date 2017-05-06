package com.example.nimira.ezrecipe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> selection = new ArrayList<String>();
    TextView text;
    CheckBox chicken, beef, rice;
    Button ingredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chicken = (CheckBox) findViewById(R.id.chicken);
        beef = (CheckBox) findViewById(R.id.beef);
        rice = (CheckBox) findViewById(R.id.rice);
        text = (TextView)findViewById(R.id.chickenTest);
        ingredients = (Button)findViewById(R.id.ingredients);
        Log.i("hi", "hello");
        String message = "abc";
//        selectItems(findViewById(android.R.id.content));
//        getIngredients();
        new CallMashapeAsync().execute(message);
    }

    public void getIngredients(View v){
        String final_selections = "";
        for (String Selections : selection){
            final_selections = final_selections + Selections + "\n";
        }
//        text.setText(final_selections);
    }



    public void selectItems(View v){
        boolean checked = ((CheckBox) v).isChecked();
        String words;
        switch (v.getId()) {
            case R.id.chicken:
                words = chicken.getText().toString();
                if (checked) {
                    selection.add(words);
                } else {
                    selection.remove(words);
                }
                break;
            case R.id.beef:
                words = beef.getText().toString();
                if (checked){
                    selection.add(words);
                }
                else{
                    selection.remove(words);
                }
                break;
            case R.id.rice:
                words = rice.getText().toString();
                if (checked){
                    selection.add(words);
                }
                else{
                    selection.remove(words);
                }
                break;
        }
    }

    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            HttpResponse<JsonNode> request = null;
            try {
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/479101/information?includeNutrition=false")
                        //.header("X-Mashape-Authorization", "IBIjQLdnrhmsh19NxOPSpjaWSVIqp1owlcfjsnmDQyC8gFp1HD")
                        .header("X-Mashape-Authorization", "gNrvLXTPTNmshsXWUXLzm7VwvkJWp1m47mVjsn5eRbKVitWD4i")
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
            String answer = response.getBody().toString();
//            TextView txtView = (TextView) findViewById(R.id.textView1);
            text.setText(answer);
        }
    }
}