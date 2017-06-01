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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> checkedIngredients = new ArrayList<String>();
    ArrayList<JSONObject> info = new ArrayList<JSONObject>();


    ArrayList<String> selection = new ArrayList<String>();
    ArrayList<String> recipeIDs = new ArrayList<>();
    ArrayList<String> recipeNames = new ArrayList<>();

    ArrayList<String> recipeImages = new ArrayList<>();
    ArrayList<String> addedIngredients = new ArrayList<>();
    Button ingredients, addIngredients, getFood, done, delete;
    EditText search;
    LinearLayout linearMain;
    CheckBox checkBox;
    Integer checkBoxCount = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String items="";
//  Buttons from before Miggy implemented checkboxes
//        chicken = (CheckBox) findViewById(chicken);
//        beef = (CheckBox) findViewById(beef);
//        rice = (CheckBox) findViewById(rice);
//        jsontext = (TextView)findViewById(R.id.chickenTest);
//        idtest = (TextView) findViewById(R.id.textView);
//  Original textview variable declaration, renamed to jsontext and idtest when used in onPostExecute
//        text = (TextView)findViewById(R.id.chickenTest);
//        test = (TextView) findViewById(R.id.textView);


        linearMain = (LinearLayout)findViewById(R.id.buttons);
        search = (EditText)findViewById(R.id.search);
        getFood = (Button)findViewById(R.id.getFood);
        done = (Button)findViewById(R.id.done);

        ingredients = (Button)findViewById(R.id.ingredients);
        delete = (Button)findViewById(R.id.delete);

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
                        //RecipeInfo info = new RecipeInfo(root.getJSONObject(i).getString("title"),root.getJSONObject(i).getString("id"));
                        recipeIDs.add(root.getJSONObject(i).getString("id"));
                        recipeNames.add(root.getJSONObject(i).getString("title"));

                        recipeImages.add(root.getJSONObject(i).getString("image"));
                    }
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("recipeIDs", recipeIDs);
                    intent.putExtra("recipeNames", recipeNames);
                    intent.putExtra("recipeImage", recipeImages);
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
                recipeImages.clear();
            }
        });

        addIngredients = (Button)findViewById(R.id.addIngredients);
        addIngredients.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addIngredients.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                getFood.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        done.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addIngredients.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                getFood.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                search.setText(null);
            }
        });

        getFood.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addedIngredients.add(search.getText().toString());
                Collections.sort(addedIngredients, String.CASE_INSENSITIVE_ORDER);
                Log.i("addedIngredients array", addedIngredients.toString());
                displayCheckBoxes();
                search.setText(null);
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                selectItems(v);
                for (int i=0; i<selection.size(); i++){
                    addedIngredients.remove(selection.get(i));
                }
                Log.i("items deleted", selection.toString());
                Log.i("ingredients left", addedIngredients.toString());
                selection.clear();
                displayCheckBoxes();
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


    public void displayCheckBoxes(){
        linearMain.removeAllViewsInLayout();
        for (int i=0; i<addedIngredients.size(); i++){
            checkBox = new CheckBox(MainActivity.this);
            checkBox.setId(i);
            checkBox.setText(addedIngredients.get(i));
            checkBox.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    selectItems(v);
                }
            });
            linearMain.addView(checkBox);
        }
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

//Grabs list of all checkboxes and adds checked boxes to an ArrayList of strings of the ingredients, saves it to selection
    public void selectItems(View v) {
        ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
        ArrayList<String> checkedIngredients = new ArrayList<String>();
        checkBoxes = getCheckBoxes();
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
//                Log.i("request", "" + request);
            } catch (UnirestException e) {
                // TO8DO Auto-generated catch block
                e.printStackTrace();
            }

            return request;
        }


        protected void onProgressUpdate(Integer...integers) {
        }

        protected void onPostExecute(HttpResponse<JsonNode> response) {

        }
    }
}
