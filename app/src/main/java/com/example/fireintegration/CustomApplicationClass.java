package com.example.fireintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;

public class CustomApplicationClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_application_class);
        Branch.enableLogging();

        // Branch object initialization
       Branch b=Branch.getAutoInstance(this);

       b.getDeviceInfo();

  String str = new BranchUniversalObject().getDescription();
        Bundle bundle= getIntent().getExtras();

        String p = bundle.getString("KK");
        TextView t= findViewById(R.id.textView4);
        t.setText(p);
//        t.setText("Hi there");
        TextView t2= findViewById(R.id.textView5);
        t2.setText(str);
    }
}