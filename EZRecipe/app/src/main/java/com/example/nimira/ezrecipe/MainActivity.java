package com.example.nimira.ezrecipe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> selection = new ArrayList<String>();
    ArrayList<String> recipeIDs = new ArrayList<>();
    ArrayList<String> recipeNames = new ArrayList<>();

    TextView text;
//    TextView test;
    CheckBox chicken, beef, rice;
    Button ingredients;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String items="";
//        text = (TextView)findViewById(R.id.chickenTest);
//        test = (TextView) findViewById(R.id.textView);
        ingredients = (Button)findViewById(R.id.ingredients);
        ingredients.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HttpResponse<JsonNode> response = new CallMashapeAsync().execute().get();
                    String data = response.getBody().toString();
                    Log.i("data", data);
                    JSONArray root = new JSONArray(data);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.setValue(data);
                    for (int i=0; i<root.length(); i++){
                        recipeIDs.add(root.getJSONObject(i).getString("id"));
                        recipeNames.add(root.getJSONObject(i).getString("title"));
                    }
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("recipeIDs", recipeIDs);
                    intent.putExtra("recipeNames", recipeNames);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recipeIDs.clear();
                recipeNames.clear();
            }
        });
    //Button for user log in ans sign up.
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, MenuLogin.class);
                    startActivity(intent);
            }

        });

    }

    public ArrayList<CheckBox> getCheckBoxes(){
        ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        findCheckBoxes(viewGroup, checkBoxes);
        return checkBoxes;
    }


    private static void findCheckBoxes(ViewGroup viewGroup, ArrayList<CheckBox> checkBoxes) {
        for (int i=0, N = viewGroup.getChildCount(); i<N; i++){
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup){
                findCheckBoxes((ViewGroup) child, checkBoxes);
            }
            else if (child instanceof CheckBox) {
                checkBoxes.add((CheckBox) child);
            }
        }
    }
    public void selectItems(View v) {
        ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
        ArrayList<String> checkedIngredients = new ArrayList<String>();
        checkBoxes = getCheckBoxes();
        boolean checked = ((CheckBox) v).isChecked();

        String words;
        Log.i("size of checkboxes", ""+checkBoxes.size());
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                checkedIngredients.add(checkBoxes.get(i).getText().toString());
            } else {
                checkedIngredients.remove(checkBoxes.get(i).getText().toString());
            }
        }
        Log.i("List", checkedIngredients.toString());
        selection = checkedIngredients;
    }

    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {
            String items = "";
            for (int i=0; i<selection.size(); i++){
                items = items + "" + selection.get(i) + "%2C";
            }
            String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients="+items+ "&limitLicense=false&number=10&ranking=1";
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