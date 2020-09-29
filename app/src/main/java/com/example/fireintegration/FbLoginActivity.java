package com.example.fireintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

public class FbLoginActivity extends AppCompatActivity  {
String MyPREFERENCES="MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_login);
        LoginButton l1= findViewById(R.id.login_button12);

        l1.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      LoginManager.getInstance().logOut();
                                      finish();
                                      SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                      SharedPreferences.Editor editor = sharedpreferences.edit();
                                      editor.putBoolean("loginCon",false);
                                      editor.commit();
                                      Intent i = new Intent(FbLoginActivity.this, MainActivity.class);
                                      startActivity(i);
                                      finish();
                                  }
                              }

        );
    }
}