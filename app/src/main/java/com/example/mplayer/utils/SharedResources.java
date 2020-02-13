package com.example.mplayer.utils;

import java.util.concurrent.atomic.AtomicReference;

public class SharedResources {

    private static SharedResources instance = null;
    private static AtomicReference<String> userId;

    private SharedResources(){
        userId = new AtomicReference<>();
    }

    public static SharedResources getInstance() {
        if(instance == null) {
            instance = new SharedResources();
        }

        return instance;
    }

    public void setUserId(String user) {
        userId.set(user);
    }

    public String getUserId() {
        return userId.get();
    }

    public void resetUserId() {
        userId.set(null);
    }
}
