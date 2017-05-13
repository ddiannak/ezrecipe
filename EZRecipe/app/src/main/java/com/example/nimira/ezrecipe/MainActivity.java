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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> selection = new ArrayList<String>();
    TextView text;
    TextView test;
    CheckBox chicken, beef, rice;
    Button ingredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String items="";
        chicken = (CheckBox) findViewById(R.id.chicken);
        beef = (CheckBox) findViewById(R.id.beef);
        rice = (CheckBox) findViewById(R.id.rice);
        text = (TextView)findViewById(R.id.chickenTest);
        test = (TextView) findViewById(R.id.textView);
        ingredients = (Button)findViewById(R.id.ingredients);

        ingredients.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new CallMashapeAsync().execute();
            }
        });
    }

//    public void getIngredients(View v){
////        String final_selections = "";
////        for (String Selections : selection){
////            final_selections = final_selections + Selections + "\n";
////        }
////        text.setText(final_selections);

//    }



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
        Log.i("List: ", selection.toString());
    }

    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {
            String items = "";
            for (int i=0; i<selection.size(); i++){
                items = items + "" + selection.get(i) + "%2C";
            }
            String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients="+items+ "&limitLicense=false&number=5&ranking=1";
            Log.i("url: ", url);
            HttpResponse<JsonNode> request = null;
            try {

                request = Unirest.get(url)
                        .header("X-Mashape-Key", "gNrvLXTPTNmshsXWUXLzm7VwvkJWp1m47mVjsn5eRbKVitWD4i")
                        .header("Accept", "application/json")
                        .asJson();
                Log.i("request", "" + request);
                Log.i("List: ", selection.toString());
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
            try {
               // JSONObject root = new JSONObject(answer);
                JSONArray root = new JSONArray(answer);
               // JSONArray recipe_name = root.getJSONArray(0);
                JSONObject id_num = root.getJSONObject(0);
                int id = id_num.getInt("id");
                String idstr = id_num.getString("id");
                test.setText(idstr);
            }
            catch (JSONException e) {
                test.setText("failed: make sure you are getting the right type");
                text.setText(answer);
            }
        }
    }
}