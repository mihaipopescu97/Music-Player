package com.example.mplayer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mplayer.R;
import com.example.mplayer.entities.Device;
import com.example.mplayer.entities.User;
import com.example.mplayer.utils.FirebaseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailId;
    private EditText password;
    private EditText deviceId;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseHandler.setInstance(FirebaseDatabase.getInstance());

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPass);
        deviceId = findViewById(R.id.deviceId);
    }

    //Sign up logic
    public void signUp(View view) {

        final String email = emailId.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String devId = deviceId.getText().toString().trim();

        if (email.isEmpty()) {
            emailId.setError("Please enter email!");
        } else if (pass.isEmpty()) {
            password.setError("Please enter password!");
        } else if (devId.isEmpty()) {
            deviceId.setError("Please enter device id!");
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "SigUp failed!", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseHandler.addUser(new User(email, pass, devId));
                        FirebaseHandler.addDevice(new Device(devId));
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    }
                }
            });
        }
    }

    //Back to main activity
    public void backToMain(View view) {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }
}
