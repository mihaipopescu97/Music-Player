package com.example.mplayer.utils.helpers;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class DownloadTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "DownloadTask";
    private static final String EXTENSION = ".mp3";
    private List<String> urls;

    private final StorageReference storageReference;
    private final WeakReference<? extends AppCompatActivity> weakReference;

    public DownloadTask(final AppCompatActivity activity, final List<String> urls) {
        weakReference = new WeakReference<>(activity);
        storageReference = FirebaseStorage.getInstance().getReference();
        this.urls = urls;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(String... strings) {
        final String[] names = Arrays.copyOfRange(strings, 1, strings.length);
        Stream.of(names).forEach(name -> {
            StorageReference reference = storageReference.child(name + EXTENSION);
            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                String url = uri.toString();
                urls.add(url);
            }).addOnFailureListener(e -> Log.e(TAG, "Download failed"));
        });

        return null;
    }
}
