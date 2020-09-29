package com.example.fireintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Dashboard extends AppCompatActivity {
String MyPREFERENCES="MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String value = getIntent().getStringExtra("stuff");
        TextView er = (TextView) findViewById(R.id.accountName);
        Log.d("GetNameMan", value);
        er.setText(value);
        Button logout = findViewById(R.id.logOut);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(Dashboard.this,gso);
                googleSignInClient.signOut();
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("loginCon",false);
                editor.commit();
                Intent i= new Intent(Dashboard.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }}