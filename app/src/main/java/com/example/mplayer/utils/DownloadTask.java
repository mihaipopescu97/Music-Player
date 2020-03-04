package com.example.mplayer.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "DownloadTask";
    private final WeakReference<? extends AppCompatActivity> weakReference;
    private final StorageReference storageReference;

    public <T extends AppCompatActivity> DownloadTask(T activity) {
        weakReference = new WeakReference<>(activity);
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected Void doInBackground(String... strings) {
        Activity activity = weakReference.get();
        Context context = activity.getBaseContext();

        for(String name : strings) {
            StorageReference reference = storageReference.child(name + ".mp3");

            reference.getDownloadUrl(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    downloadFile(context, name, ".mp3", DIRECTORY_DOWNLOADS, url);
                }
            }).addOnSuccessListener(uri -> Log.i(TAG, "Success.")).addOnFailureListener(e -> Log.e(TAG, "Fail."));
        }
        return null;
    }

    private void downloadFile(Context context, String fileName, String fileExtension,
                              String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadManager.enqueue(request);
    }
}
