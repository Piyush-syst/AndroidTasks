package com.example.fireintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
//import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
//import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
int f=1,f2=1;
    CallbackManager c,c1;
 static   String str;
//    private static final String EMAIL = "email";
    private static final String public_profile = "public_profile";
// ...
// Initialize Firebase Auth
  private FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    static String personName="Amzaing ";
GoogleApiClient gap;
 //   mAuth = FirebaseAuth.getInstance();
    private static final int RC_SIGN_IN = 0;
    GoogleSignInClient g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            // Branch logging for debugging
//        Branch.enableLogging();
//
//            // Branch object initialization
//        Branch.getAutoInstance(this);
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("Token", newToken);
            }
        });
        if (AccessToken.getCurrentAccessToken() != null) {
            Intent i= new Intent(MainActivity.this,FbLoginActivity.class);
            startActivity(i);
        }
        if (gap != null && gap.isConnected()) {
            // signed in. Show the "sign out" button and explanation.
            Intent i= new Intent(MainActivity.this,Dashboard.class);
            startActivity(i);
        }
//        String newToken = instanceIdResult.getToken();
        //  GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        g = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    signIn();
                    break;
                // ...
            }
        }});
        Button b1= findViewById(R.id.trackMe);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i= new Intent(MainActivity.this, MoveTrack.class);
                startActivity(i);
            }
        });
//        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            finish();
//        }
//        int permission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permission == PackageManager.PERMISSION_GRANTED) {
//            startTrackerService();
//        } else {
//
////If the app doesn’t currently have access to the user’s location, then request access//
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST);
//        }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
//            grantResults) {
//
////If the permission has been granted...//
//
//        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
////...then start the GPS tracking service//
//
//            startTrackerService();
//        } else {
//
////If the user denies the permission request, then display a toast with some more information//
//
//            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
//        }
//    }
        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FirebaseCrashlytics.getInstance().log("Higgs-Boson detected! Bailing out");
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

         c= CallbackManager.Factory.create();
        c1= CallbackManager.Factory.create();
   LoginButton loginButton = findViewById(R.id.login_button);

        LoginButton loginButton2 = findViewById(R.id.login_button1);
        loginButton2.setReadPermissions(Arrays.asList(public_profile));
        loginButton.setReadPermissions(Arrays.asList(public_profile));
        Log.e("Letus","Fb");
        loginButton2.registerCallback(c, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult2) {

                Log.d("Yoo", "facebook:onSuccess:" + new Gson().toJson(loginResult2));

                handleFacebookAccessToken(loginResult2.getAccessToken());

            }


            @Override
            public void onCancel() {
                Log.d("Upp", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Downn", "facebook:onError", error);
                // ...
            }
        });
//        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setReadPermissions(Arrays.asList(public_profile));
       Log.e("Letus","Fb");
        loginButton.registerCallback(c1, new FacebookCallback<LoginResult>() {

                @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("PP", "facebook:onSuccess:" + new Gson().toJson(loginResult));

               f2=2;

            }


            @Override
            public void onCancel() {
                Log.d("Upp", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Downn", "facebook:onError", error);
                // ...
            }
        });

// ...
        GoogleSignInOptions gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(this, gso1);
        SignInButton signInButton1 = findViewById(R.id.sign_in_button1);
        signInButton1.setSize(SignInButton.SIZE_STANDARD);
        signInButton1.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sign_in_button1:

                    signIn1();
                    break;
                // ...
            }
        }});
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//
//            // Pass the activity result back to the Facebook SDK
//           c.onActivityResult(requestCode, resultCode, data);
//        }

    }


    //   @Override
 //   public void onClick(View v) {
   //     switch (v.getId()) {
     //       case R.id.sign_in_button:
       //         signIn();
       //         break;
            // ...
    //    }
   // }
    private void signIn() {
        Intent signInIntent = g.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signIn1() {
        f=2;
        Intent signInIntent = g.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     //Log.e("Debug.....", "onActivityResult: "+data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN  && f==2) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
       else if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            Intent i= new Intent(new Intent(this, Dashboard.class));
//
//            i.putExtra("stuff",personName);
//            startActivity(i);

            try {
                // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
           ;
       //    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        //   Log.e("Yahi to chahiye", "firebaseAuthWithGoogle:" + account.getIdToken());
                if (account != null) {
                    personName = account.getDisplayName();
//                    String personGivenName = account.getGivenName();
//                    String personFamilyName = account.getFamilyName();
//                    String personEmail = account.getEmail();
//                    String personId = account.getId();
//                    Uri personPhoto = account.getPhotoUrl();
                }
         //       Log.e("23323Yahi to chahiye", "firebaseAuthWithGoogle:" + personName);
        //        Log.e("IDTOKENFirst", "firebaseAuthWithGoogle: "+new Gson().toJson(account));
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (Exception e) {
                // Google Sign In failed, update UI appropriately
           //     Log.e("Yai to chahiye", "Google sign in failed", e);
                // ...
            }
        }
       else if(f2==2)
        {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            c.onActivityResult(requestCode, resultCode, data);
            Intent j= new Intent(new Intent(MainActivity.this, FbLoginActivity.class));
            startActivity(j);
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            c.onActivityResult(requestCode, resultCode, data);
            Intent j= new Intent(new Intent(MainActivity.this, FbLoginActivity.class));
            startActivity(j);
        }
        }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TokenRecieved", "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Fb Success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("Fb failed", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]
        Log.e("IDTOKEN", "firebaseAuthWithGoogle: "+idToken );
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             Log.d("AG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent j= new Intent(new Intent(MainActivity.this, Dashboard.class));

                            j.putExtra("stuff",personName);
                            startActivity(j);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("TAG", "signInWithCredential:failure", task.getException());


                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String personEmail = account.getEmail();
            Intent j= new Intent(new Intent(MainActivity.this, Dashboard.class));

            j.putExtra("stuff",personEmail);
            startActivity(j);
            // Signed in successfully, show authenticated UI.

//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Without", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }
    private void startTrackerService() {
        startService(new Intent(this, MoveTrack.class));

//Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();

//Close MainActivity//

        finish();
    }
    @Override public void onStart() {
        super.onStart();
       Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();

      str = new BranchUniversalObject().getDescription();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
    }
    private Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
        @Override
        public void onInitFinished(final JSONObject linkProperties, BranchError error) {

    if(linkProperties==null)
        {
            Toast.makeText(getApplicationContext(),"JSON is null",Toast.LENGTH_LONG).show();
        }
            try {
                str=linkProperties.getString("og_title");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
            try {
                str= linkProperties.getString("canonical_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            findViewById(R.id.branchB).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){   Intent intent = new Intent(MainActivity.this, CustomApplicationClass.class);
//        intent.putExtra("branch_force_new_session", true);
//                    str= linkProperties.toString();
                    intent.putExtra("KK", linkProperties.toString());
                    startActivity(intent);
                }

            });

//            str= linkProperties.toString();
//            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();

            // do stuff with deep link data (nav to page, display content, etc)
        }
    };


}