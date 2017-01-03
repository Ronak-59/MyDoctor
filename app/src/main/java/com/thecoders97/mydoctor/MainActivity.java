package com.thecoders97.mydoctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN =0;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
         auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            //User Already Signed in
            Log.d("AUTH", auth.getCurrentUser().getEmail());
            startActivity(new Intent(this, Navigation.class));
            finish();
            return;


        }
        else{
            startActivityForResult(AuthUI.getInstance()

                    .createSignInIntentBuilder()
                    .setProviders(
                            AuthUI.GOOGLE_PROVIDER,
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.EMAIL_PROVIDER)
                    .setTheme(R.style.AuthBackground)

                    .build(),RC_SIGN_IN);

        }

        findViewById(R.id.log_out_button).setOnClickListener(this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(view.getId()==R.id.fab){
                        AuthUI.getInstance()
                                .signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("AUTH", "USER LOGGED OUT");
                                        finish();
                                    }
                                });

                    }
                }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                //User logged in
                Log.d("AUTH", auth.getCurrentUser().getEmail());
                startActivity(new Intent(this, Navigation.class));
                finish();
                return;
            }
            else if (resultCode == RESULT_CANCELED){
                //User not Authenticated
                Log.d("AUTH", "NOT AUTHENTICATED");
                finish();
            }
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.fab){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("AUTH", "USER LOGGED OUT");
                            finish();
                        }
                    });

        }
    }
}

