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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
   // TextView viewfail;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Intent intent = getIntent();
        //ArrayList<String> recipeIDs = (ArrayList<String>) intent.getSerializableExtra("recipeIDs");
        //ArrayList<String> recipeNames = (ArrayList<String>) intent.getSerializableExtra("recipeNames");
        //final DatabaseReference mDatabase;
        // mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //sets value, adds list of new recipe searches to previous list.
        //Spinner recipes = (Spinner)findViewById(R.id.recipeDropdown);

        // Check if user is signed in (non-null) and update
        FirebaseUser currentUser = firebaseAuth.getInstance().getCurrentUser();
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        recipeIDs = (ArrayList<String>) intent.getSerializableExtra("recipeIDs");
        recipeNames = (ArrayList<String>) intent.getSerializableExtra("recipeNames");
        recipeImages = (ArrayList<String>) intent.getSerializableExtra("recipeImage");
        recipes = (Spinner)findViewById(R.id.recipeDropdown);
        //ref.setValue(recipeIDs);
        //ref.push().setValue(recipeIDs);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipeNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipes.setAdapter(dataAdapter);

        Button getRecipe = (Button)findViewById(R.id.getRecipe);
        //Button saveRecipe = (Button) findViewById(R.id.saverecipe);
        image = (ImageView)findViewById(R.id.image);
       // viewfail = (TextView) findViewById(R.id.viewfail);

        getRecipe.setOnClickListener(new View.OnClickListener(){
            String data="";
            @Override
            public void onClick(View v){

                String getselected = recipes.getSelectedItem().toString();
                HttpResponse<JsonNode> response = null;
                try {
                    for (int i = 0; i < recipeNames.size(); i++) {
                        if (recipeNames.get(i).matches(getselected)) {
                            response = new CallMashapeAsync().execute(recipeIDs.get(i)).get();
                        }
                    }
                    data = response.getBody().toString();
                    //  JSONArray root = new JSONArray(data);
                    JSONObject root = new JSONObject(data);
                    // JSONArray recipe_name = root.getJSONArray(0);
                    //  JSONObject id_num = root.getJSONObject("sourceUrl");
                    String idstr = root.getString("sourceUrl");
                    // infoview.setText(idstr);
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(idstr));
                    startActivity(i);
                    //   } catch (Exception e) {
                    //      e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.i("failed:", " make sure you are getting the right type");
                    //  infoview.setText(answer);
                } catch (InterruptedException | ExecutionException e) {
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

       /* saveRecipe.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data ="fail";
                String idstr="idstr";
                viewfail.setText("tryset");
                try {
                    HttpResponse<JsonNode> response = new MenuActivity.CallMashapeAsync().execute(recipeIDs.get(position)).get();
                    data = response.getBody().toString();
                    // JSONArray root = new JSONArray(data);
                    JSONObject root = new JSONObject(data);
                    // JSONArray recipe_name = root.getJSONArray(0);
                    //  JSONObject id_num = root.getJSONObject("sourceUrl");
                    idstr = root.getString("sourceUrl");
                    //               ref.setValue(data);
                    //                  ref.setValue(recipeIDs);
                    //                 ref.push().setValue(recipeIDs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    viewfail.setText(uid);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    viewfail.setText(uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                    viewfail.setText(uid);
                }
                try{
                    //    mDatabase.child(uid).push().setValue(data);
                    ref.child(uid).push().setValue(idstr);
                    //            ref.push().setValue(data);
                    viewfail.setText(data);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    viewfail.setText("can'tpush");
                }
            }
        });*/
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

        }
    }

}

