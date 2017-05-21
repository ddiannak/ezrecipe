package com.example.nimira.ezrecipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        String value = intent.getStringExtra("foodID");
        TextView text = (TextView)findViewById(R.id.secondPage);
        text.setText(value);
        text.setText(value);

    }




}
