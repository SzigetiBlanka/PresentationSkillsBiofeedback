package com.aut.presentationskills.ui.signin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aut.presentationskills.MainActivity;
import com.aut.presentationskills.PresentationSkillsVRandAndroid;
import com.aut.presentationskills.R;
import com.aut.presentationskills.communication.MyFireBaseMessagingService;
import com.aut.presentationskills.ui.measurement.MeasurementActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;

import javax.inject.Inject;

public class SignInActivity extends AppCompatActivity implements SignInScreen, View.OnClickListener {

    @Inject
    SignInPresenter signInPresenter;

    public static String accessToken;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_GET_AUTH_CODE = 9003;
    public static final String KEY_JWT= "jwt";
    ProgressDialog dialog;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    public static GoogleSignInAccount account;
    public Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyFireBaseMessagingService();
        setContentView(R.layout.activity_sign_in);

        PresentationSkillsVRandAndroid.injector.inject(this);

        //orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Button listeners
        findViewById(R.id.sign_in_btn).setOnClickListener(this);
        findViewById(R.id.sign_out_btn).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        //Button signInButton = findViewById(R.id.sign_in_btn);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        //signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // [END customize_button]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    @Override
    public void onStart() {
        super.onStart();
        signInPresenter.attachScreen(this);
        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Context context = getApplicationContext();
            CharSequence text = "Current user: "+ currentUser.getEmail();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        updateUI(currentUser);
        // [END on_start_sign_in]
    }

    @Override
    protected void onStop() {
        super.onStop();
       signInPresenter.detachScreen();
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //handleSignInResult(task);
            try {
                // Google Sign In was successful, authenticate with Firebase
                CreateDialog();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void CreateDialog() {
        dialog=new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    private void HideDialog(){
        dialog.hide();
    }
    // [END onActivityResult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String scope = "oauth2:"+Scopes.EMAIL+" "+ Scopes.PROFILE;
                                        accessToken = GoogleAuthUtil.getToken(getApplicationContext(), acct.getEmail(), scope, new Bundle());
                                        Log.d(TAG, "accessToken:"+accessToken); //accessToken:ya29.Gl...
                                        updateUI(user);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (GoogleAuthException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            AsyncTask.execute(runnable);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            updateUI(null);
                        }

                    }
                })
        ;
    }
    // [END auth_with_google]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    // [START_EXCLUDE]
                    updateUI(null);
                    Context context = getApplicationContext();
                    CharSequence text = "Successfully sign out!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    // [END_EXCLUDE]
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, task -> {
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable FirebaseUser account) {
        if (account != null) {
            //accessTokenModel.setAccessToken(accessToken);
            if(accessToken!= null) {
                signInPresenter.refreshUser(accessToken);
            }
        } else {
            findViewById(R.id.sign_in_btn).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_btn:
                signIn();
                break;
            case R.id.sign_out_btn:
                signOut();
                break;
        }
    }

    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void startMeasurement(String jwt) {
        if(dialog!=null)
            HideDialog();
        Intent intent = new Intent(this, MeasurementActivity.class);
        intent.putExtra(KEY_JWT, signInPresenter.accessTokenInteractor.event.getJwt());
        startActivity(intent);
    }

    @Override
    public void showNetworkError(String errorMsg) {

    }
}
