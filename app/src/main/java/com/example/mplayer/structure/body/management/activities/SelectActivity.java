package com.example.mplayer.structure.body.management.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mplayer.R;
import com.example.mplayer.structure.login.MainActivity;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.enums.LogMessages;
import com.example.mplayer.utils.enums.PlayType;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//TODO needs testing then freeze
public class SelectActivity extends AppCompatActivity {

    private final String TAG = "SelectActivity";

    private FirebaseHandler firebaseHandler;
    private FirebaseAuth firebaseAuth;

    private Button singleBtn;
    private Button familyBtn;
    private AtomicReference<String> userId;
    private AtomicReference<String> email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        singleBtn = findViewById(R.id.singleBtn);
        familyBtn = findViewById(R.id.familyBtn);

        firebaseHandler = FirebaseHandler.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userId = new AtomicReference<>();
        email = new AtomicReference<>();

        new BackgroundTask(this).execute();
    }

    public void single(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(getBaseContext(), BaseActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString())
                    .putExtra("playType", PlayType.SINGLE.label);
            startActivity(intent);
        } else {
           Log.e(TAG, LogMessages.EMAIL_FETCH_ERROR.label);
        }
    }

    public void family(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(getBaseContext(), BaseActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString())
                    .putExtra("playType", PlayType.FAMILY.label);
            startActivity(intent);
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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SelectActivity activity = weakReference.get();

            Log.i(activity.TAG, LogMessages.ASYNC_START.label);
            List<Button> list = Arrays.asList(
                    activity.singleBtn,
                    activity.familyBtn);
            list.forEach(button -> button.setVisibility(View.GONE));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SelectActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.email.set(intent.getStringExtra("email"));
            activity.firebaseHandler.getUserId(activity.email.get(), activity.userId);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SelectActivity activity = weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_END.label);
            List<Button> list = Arrays.asList(
                    activity.singleBtn,
                    activity.familyBtn);
            list.forEach(button -> button.setVisibility(View.VISIBLE));
        }
    }
}