package com.example.nimira.ezrecipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by miggycalleja on 5/15/17.
 */

public class MenuActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        text = (TextView)findViewById(R.id.secondPage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        ArrayList<JSONObject> value = (ArrayList<JSONObject>) getIntent().getSerializableExtra("foodID");
        TextView text = (TextView)findViewById(R.id.secondPage);
        text.setText(value.toString());

    }




}
