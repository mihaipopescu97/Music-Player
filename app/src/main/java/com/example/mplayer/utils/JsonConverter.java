package com.example.mplayer.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonConverter {

    private static final String TAG = "JsonConverter";

    private static JsonConverter instance = null;

    private JsonConverter() {
        Log.d(TAG, "Json converter instance created");
    }

    public static synchronized JsonConverter getInstance() {
        if(instance == null) {
            instance = new JsonConverter();
        }

        return instance;
    }

    public synchronized JSONObject convert(String message) {

        JSONObject object;

        try {
            object = new JSONObject(message);
            Log.d(TAG, "Converting message " + message + " into Json");
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
