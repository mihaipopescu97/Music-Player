package com.example.mplayer.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mplayer.R;
import com.example.mplayer.activities.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText emailId, password, deviceId;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Main activity started");

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPass);
        deviceId = findViewById(R.id.signUpDev);

        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Please login!", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    //Login button functionality
    public void login(View view) {

        Log.d(TAG, "Login started");
        String email = emailId.getText().toString();
        String pass = password.getText().toString();
        String devId = deviceId.getText().toString();

        if(email.isEmpty()) {
            emailId.setError("Please enter email!");
        } else if(pass.isEmpty()) {
            password.setError("Please enter password!");
        } else if (devId.isEmpty()) {
            deviceId.setError("Please enter device id!");
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(MainActivity.this, HomeActivity.class);

                        Bundle bundle = new Bundle();

                        bundle.putString("devId", deviceId.toString());
                        startActivity(i);
                    }
                }
            });
        }


    }

    //Sign up text field functionality
    public void goToSignUp(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }


}