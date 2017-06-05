package com.example.nimira.ezrecipe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> selection = new ArrayList<String>();
    ArrayList<String> recipeIDs = new ArrayList<>();
    ArrayList<String> recipeNames = new ArrayList<>();
    ArrayList<String> recipeImages = new ArrayList<>();
    ArrayList<String> addedIngredients = new ArrayList<>();
    Button ingredients, addIngredients, getFood, done, delete, logout;
//    EditText search;
    AutoCompleteTextView autoComplete;
    private static final String[] auto = new String[] {
            "beef", "chicken", "salmon", "cheese", "butter", "tomato", "potato", "cilantro", "broccoli"
            , "lettuce", "mushroom", "oregano", "rice", "bread", "pork", "salt", "onion", "olive oil", "garlic"
            , "soy sauce", "pepper", "carrots", "red bell pepper", "ginger", "turkey", "spinach", "water", "sesame oil"
            , "sour cream", "cayenne pepper", "corn starch", "watermelon", "strawberry", "milk", "flour", "eggs"
    };
    LinearLayout linearMain;
    CheckBox checkBox;
    String email, uid;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        linearMain = (LinearLayout) findViewById(R.id.buttons);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, auto);

//        search = (EditText) findViewById(R.id.search);
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        autoComplete.setAdapter(adapter);
        getFood = (Button) findViewById(R.id.getFood);
        done = (Button) findViewById(R.id.done);
        ingredients = (Button) findViewById(R.id.ingredients);
        delete = (Button) findViewById(R.id.delete);
        logout = (Button) findViewById(R.id.logout);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            email = user.getEmail();
            uid = user.getUid();
        } else {
            // No user is signed in
        }

        //get ingredients from firebase users and display
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> ingredients = new ArrayList<>();
                for (DataSnapshot i: dataSnapshot.getChildren()){
                    IngredientsList list = i.getValue(IngredientsList.class);
                    if (list.getUserId().equals(uid)) {
                        ingredients = list.getIngredients();
                    }
                }
                addedIngredients = ingredients;
                if (addedIngredients!=null) {
                    displayCheckBoxes();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //button click for first api call to get list of recipes
        ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HttpResponse<JsonNode> response = new CallMashapeAsync().execute().get();
                    String data = response.getBody().toString();
                    JSONArray root = new JSONArray(data);
                    for (int i = 0; i < root.length(); i++) {
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

        //button click to show search bar to add ingredient
        addIngredients = (Button) findViewById(R.id.addIngredients);
        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredients.setVisibility(View.GONE);
//                search.setVisibility(View.VISIBLE);
                autoComplete.setVisibility(View.VISIBLE);
                getFood.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        //button click to hide search bar
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addIngredients.setVisibility(View.VISIBLE);
//                search.setVisibility(View.GONE);
                autoComplete.setVisibility(View.GONE);
                getFood.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                autoComplete.setText(null);
//                search.setText(null);
            }
        });

        //button click to add ingredient to firebase as json and view as checkbox
        getFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addedIngredients.contains(autoComplete.getText().toString())) {
                    addedIngredients.add(autoComplete.getText().toString());
                    Collections.sort(addedIngredients, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            if (o1 == null && o2 == null) {
                                return 0;
                            }
                            if (o1 == null) {
                                return 1;
                            }
                            if (o2 == null) {
                                return -1;
                            }
                            return o1.compareTo(o2);
                        }
                    });
                    displayCheckBoxes();
//                    search.setText(null);
                    autoComplete.setText(null);

                    IngredientsList food = new IngredientsList(addedIngredients, uid, email);
                    mDatabase.child(uid).setValue(food);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Duplicate Ingredient!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //button click to delete ingredient from firebase and view
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectItems(v);
                mDatabase.child(uid).child("ingredients").removeValue();
                for (int i = 0; i < selection.size(); i++) {
                    addedIngredients.remove(selection.get(i));
//                    Log.i("index", String.valueOf(addedIngredients.indexOf(selection.get(i))));
                    addedIngredients.removeAll(Collections.singleton(null));
                }
                if (addedIngredients.size()==0){
                    mDatabase.child(uid).removeValue();
                }
                else {
                    mDatabase.child(uid).setValue(new IngredientsList(addedIngredients, uid, email));
                }
//                Log.i("items deleted", selection.toString());
//                Log.i("ingredients left", addedIngredients.toString());
                selection.clear();

                displayCheckBoxes();
            }
        });

        //button click to go to login activity
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Log.i("email after logout", email);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }

    public void displayCheckBoxes(){
        linearMain.removeAllViewsInLayout();
        for (int i=0; i<addedIngredients.size(); i++){
            if (addedIngredients.get(i)!=null) {
                checkBox = new CheckBox(MainActivity.this);
                checkBox.setId(i);
                checkBox.setText(addedIngredients.get(i));
                checkBox.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        selectItems(v);
                    }
                });
                linearMain.addView(checkBox);
            }
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
//        Log.i("List", checkedIngredients.toString());
        selection = checkedIngredients;
    }


    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {
            String items = "";
            for (int i=0; i<selection.size(); i++){
                items = items + "" + selection.get(i) + "%2C";
            }
            String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients="+items+ "&limitLicense=false&number=10&ranking=1";
//            Log.i("url: ", url);
            HttpResponse<JsonNode> request = null;
            try {

                request = Unirest.get(url)
                        .header("X-Mashape-Key", "gNrvLXTPTNmshsXWUXLzm7VwvkJWp1m47mVjsn5eRbKVitWD4i")
                        .header("Accept", "application/json")
                        .asJson();
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