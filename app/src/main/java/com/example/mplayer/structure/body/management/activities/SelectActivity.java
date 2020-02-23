package com.example.mplayer.structure.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mplayer.R;
import com.example.mplayer.structure.login.MainActivity;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;
import com.example.mplayer.utils.enums.PlayType;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

//FROZEN
public class SelectActivity extends AppCompatActivity {

    private final String TAG = "SelectActivity";

    private FirebaseHandler firebaseHandler;
    private FirebaseAuth firebaseAuth;
    private SharedResources resources;

    private AtomicReference<String> userId;
    private AtomicReference<String> email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        firebaseHandler = FirebaseHandler.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        resources = SharedResources.getInstance();

        userId = new AtomicReference<>();
        email = new AtomicReference<>();

        new BackgroundTask(this).execute();

        while(userId.get().isEmpty()) {
            Log.i(TAG, "Waiting for user fetch..");
        }

        resources.setUserId(userId.get());
    }

    @Override
    protected void onStart() {
        super.onStart();
        resources.resetPlayType();
    }

    public void single(View view) {
        if(!userId.get().isEmpty()) {
            resources.setPlayType(PlayType.SINGLE.label);
            startActivity(new Intent(getBaseContext(), BaseActivity.class));
        } else {
           Log.e(TAG, LogMessages.EMAIL_FETCH_ERROR.label);
        }
    }

    public void family(View view) {
        if(!userId.get().isEmpty()) {
            resources.setPlayType(PlayType.FAMILY.label);
            startActivity(new Intent(getBaseContext(), BaseActivity.class));
        } else {
            Log.e(TAG, LogMessages.EMAIL_FETCH_ERROR.label);
        }
    }

    public void logOutFromSelect(View view) {
        firebaseAuth.signOut();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<SelectActivity> weakReference;

        BackgroundTask(SelectActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SelectActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.email.set(intent.getStringExtra("email"));
            if(activity.email.get() != null) {
                activity.firebaseHandler.getUserIdFromEmail(activity.email.get(), activity.userId);
            } else  {
                Log.e(activity.TAG, "Email pass error");
            }
            return null;
        }
    }
}
