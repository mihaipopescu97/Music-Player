package com.example.mplayer.utils.helpers;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.stream.Stream;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "DownloadTask";
    private static final String EXTENSION = ".mp3";

    private final StorageReference storageReference;
    private final WeakReference<? extends AppCompatActivity> weakReference;

    //UHHHH
    public DownloadTask(AppCompatActivity activity) {
        weakReference = new WeakReference<>(activity);
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(String... strings) {
        Stream.of(strings).forEach(name -> {
            StorageReference reference = storageReference.child(name + EXTENSION);
            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                String url = uri.toString();
                downloadFiles(weakReference.get().getBaseContext(), name, url);
            }).addOnFailureListener(e -> Log.e(TAG, "Download failed"));
        });

        return null;
    }

    private void downloadFiles(Context context, String fileName, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, DIRECTORY_DOWNLOADS, fileName + EXTENSION);

        downloadManager.enqueue(request);
    }
}
