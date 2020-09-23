package com.example.fireintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

// ...
// Initialize Firebase Auth
  private FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    static String personName=" ";

 //   mAuth = FirebaseAuth.getInstance();
    private static final int RC_SIGN_IN = 0;
    GoogleSignInClient g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        g = GoogleSignIn.getClient(this, gso);
        //  GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
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
        // Initialize Facebook Login button
        CallbackManager c= CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(c, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("Yoo", "facebook:onSuccess:" + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken());

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Yahi to chahiye", "firebaseAuthWithGoogle:" + account.getId());
             //   GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
                if (account != null) {
                    personName = account.getDisplayName();
                    String personGivenName = account.getGivenName();
                    String personFamilyName = account.getFamilyName();
                    String personEmail = account.getEmail();
                    String personId = account.getId();
                    Uri personPhoto = account.getPhotoUrl();
                }
              Intent i= new Intent(new Intent(this, Dashboard.class));

              i.putExtra("stuff",personName);
               startActivity(i);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("Yai to chahiye", "Google sign in failed", e);
                // ...
            }
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("That's it", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Hogya", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("nahihua", "signInWithCredential:failure", task.getException());


                        }

                        // ...
                    }
                });
    }


}