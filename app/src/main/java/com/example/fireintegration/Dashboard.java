package com.example.fireintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String value = getIntent().getStringExtra("stuff");
      TextView  er=(TextView)findViewById(R.id.accountName);
        Log.d("GetNameMan", value);
      er.setText(value);
    }

}