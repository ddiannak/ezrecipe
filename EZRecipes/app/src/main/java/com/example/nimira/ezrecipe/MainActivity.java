package com.example.nimira.ezrecipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String query = "";
    TextView chickenTest;
    Button chicken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chicken = (Button)findViewById(R.id.chicken);
        selectChicken();

    }

    public void selectChicken(){
        chicken.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.d("message", "I'm pressed");
                    }
                }
        );
    }
}
