package com.example.mplayer.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class StorageHandler {

    private static StorageHandler instance = null;
    private StorageReference storage;
    private StorageReference storageReference;

    private StorageHandler() {
        storage = FirebaseStorage.getInstance().getReference();
        storageReference = storage.child("/music");
    }

    public StorageHandler getInstance() {
        if(instance == null) {
            instance = new StorageHandler();
        }

        return instance;
    }

    public void write(String path) {
        Uri file = Uri.fromFile(new File(path));
        storageReference.putFile(file)
                .addOnSuccessListener(taskSnapshot -> {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                }).addOnFailureListener(e -> {

                })
    }
    public void read(AtomicReference<File> localFile) {
            //File localFile = File.createTempFile("music", "mp3");
        storageReference.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {

                }).addOnFailureListener(e -> {

                });
    }
}
