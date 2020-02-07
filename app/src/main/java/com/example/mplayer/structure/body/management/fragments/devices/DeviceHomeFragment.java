package com.example.mplayer.structure.body.management.fragments.devices;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.settings.DeviceSettingsActivity;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

public class DeviceHomeFragment extends Fragment {

    private final String TAG = "DeviceHomeFragment";
    private AtomicReference<String> prevActivity = new AtomicReference<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_home, container, false);
        Log.d(TAG, "Device home fragment started");

        final Button addDeviceBtn = view.findViewById(R.id.deviceHomeAddBtn);
        final Button deleteDeviceBtn = view.findViewById(R.id.deviceHomeDeleteBtn);
        final Button backBtn = view.findViewById(R.id.deviceHomeBackBtn);

        prevActivity = new AtomicReference<>();

        new BackgroundTasks(this).execute();

        addDeviceBtn.setOnClickListener(v -> {
            if(getActivity() != null) {
                Log.d(TAG, LogMessages.CHANGE_ADD.label);
                ((DeviceSettingsActivity)getActivity()).setViewPager(2);
            } else {
                Log.e(TAG, LogMessages.ACTIVITY_NULL.label);
            }
        });

        deleteDeviceBtn.setOnClickListener(v -> {
            if(getActivity() != null) {
                Log.d(TAG, LogMessages.CHANGE_DELETE.label);
                ((DeviceSettingsActivity)getActivity()).setViewPager(3);
            } else {
                Log.e(TAG, LogMessages.ACTIVITY_NULL.label);
            }
        });

        backBtn.setOnClickListener(v -> {
            Log.d(TAG, LogMessages.CHANGE_DEFAULT.label + prevActivity.get());
            try {
                startActivity(new Intent(getActivity(), Class.forName(prevActivity.get())));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceHomeFragment> weakReference;

        BackgroundTasks(DeviceHomeFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DeviceHomeFragment fragment = weakReference.get();

            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DeviceHomeFragment fragment = weakReference.get();

            Log.d(fragment.TAG, LogMessages.ASYNC_END.label);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DeviceHomeFragment fragment = weakReference.get();

            Log.d(fragment.TAG, LogMessages.ASYNC_WORKING.label);
            Bundle bundle = fragment.getArguments();
            if(bundle != null) {
                fragment.prevActivity.set(bundle.getString("prevActivity"));
            } else {
                //TODO new LOG MESSAGE
                Log.e(fragment.TAG, LogMessages.USER_FETCH_ERROR.label);
            }

            return null;
        }
    }
}

