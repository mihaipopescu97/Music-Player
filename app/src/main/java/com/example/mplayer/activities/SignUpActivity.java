package com.example.mplayer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailId;
    private EditText password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPass);
    }

    //Sign up logic
    public void signUp(View view) {
        String email = emailId.getText().toString();
        String pass = password.getText().toString();

        if (email.isEmpty()) {
            emailId.setError("Please enter email!");
        } else if (pass.isEmpty()) {
            password.setError("Please enter password!");
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "SigUp failed!", Toast.LENGTH_SHORT).show();
                    } else {
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
