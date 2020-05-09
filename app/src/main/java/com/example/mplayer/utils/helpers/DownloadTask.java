package com.example.mplayer.utils.helpers;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.stream.Stream;


public class DownloadTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "DownloadTask";
    private static final String EXTENSION = ".mp3";
    private List<String> urls;

    private final StorageReference storageReference;

    public DownloadTask(final List<String> urls) {
        storageReference = FirebaseStorage.getInstance().getReference();
        this.urls = urls;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(String... strings) {
        Stream.of(strings).forEach(name -> {
            StorageReference reference = storageReference.child(name + EXTENSION);
            reference.getDownloadUrl().addOnSuccessListener(uri -> urls.add(uri.toString())).addOnFailureListener(e -> Log.e(TAG, "Download failed"));
        });
        urls.forEach(url -> Log.i(TAG, url));
        return null;
    }
}
