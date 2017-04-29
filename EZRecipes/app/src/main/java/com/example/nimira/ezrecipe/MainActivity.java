package com.example.nimira.ezrecipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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

//        selectItems(findViewById(android.R.id.content));
//        getIngredients();
    }

    public void getIngredients(View v){
        String final_selections = "";
        for (String Selections : selection){
            final_selections = final_selections + Selections + "\n";
        }
        text.setText(final_selections);
    }



    public void selectItems(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()) {
            case R.id.chicken:
                String cwords = chicken.getText().toString();
                if (checked) {
                    selection.add(cwords);
                } else {
                    selection.remove(cwords);
                }
                break;
            case R.id.beef:
                String bwords = beef.getText().toString();
                if (checked){
                    selection.add(bwords);
                }
                else{
                    selection.remove(bwords);
                }
                break;
            case R.id.rice:
                String rwords = rice.getText().toString();
                if (checked){
                    selection.add(rwords);
                }
                else{
                    selection.remove(rwords);
                }
                break;
        }
    }


}
