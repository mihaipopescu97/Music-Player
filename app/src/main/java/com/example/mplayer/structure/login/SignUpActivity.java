package com.example.mplayer.structure.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mplayer.R;
import com.example.mplayer.entities.User;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.enums.LogMessages;
import com.google.firebase.auth.FirebaseAuth;

//TODO ADD INFO
public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText emailId;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        firebaseHandler = FirebaseHandler.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPass);
    }

    //Sign up logic
    public void signUp(View view) {

        Log.d(TAG, "New sign up attempt");

        final String email = emailId.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        if (email.isEmpty()) {
            emailId.setError("Please enter email!");
        } else if (pass.isEmpty()) {
            password.setError("Please enter password!");
        }  else {
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, task -> {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "New user added with email:" + email + " and pass:" + pass);
                    Toast.makeText(SignUpActivity.this, "Sig Up failed!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Creating user with email:" + email + " and password:" + pass);
                    firebaseHandler.addUser(new User(email, pass));

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            });
        }
    }

    //Back to main activity
    public void backToMain(View view) {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }
}
