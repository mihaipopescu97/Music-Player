package com.example.mplayer.structure.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.SelectActivity;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText emailId, password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Main activity started");

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPass);
    }


    //Login button functionality
    public void login(View view) {

        Log.d(TAG, "Login started");
        final String email = emailId.getText().toString();
        final String pass = password.getText().toString();

        if(email.isEmpty()) {
            emailId.setError("Please enter email!");
        } else if(pass.isEmpty()) {
            password.setError("Please enter password!");
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, task -> {
                Log.d(TAG, "Signing in user with email:" + email + " and password:" + pass);
                if(!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getBaseContext(), SelectActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            });
        }


    }

    //Sign up text field functionality
    public void goToSignUp(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }


}
