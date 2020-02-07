package com.example.mplayer.structure.body.management.fragments.devices;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.settings.DeviceSettingsActivity;
import com.example.mplayer.entities.Device;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DeviceSelectFragment extends Fragment {

    private final String TAG = "DeviceSelectFragment";
    private FirebaseHandler firebaseHandler;

    private AtomicReference<String> userId;
    private AtomicReference<String> prevActivity;
    private List<Device> devices;
    private List<String> devicesId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_select, container, false);

        Log.d(TAG, LogMessages.ACTIVITY_START.label);

        firebaseHandler = FirebaseHandler.getInstance();

        final Spinner devicesSpinner = view.findViewById(R.id.deviceSelectSpinner);
        final Button selectBtn = view.findViewById(R.id.deviceSelectBtn);
        final Button backBtn = view.findViewById(R.id.deviceSelectBackBtn);

        userId = new AtomicReference<>();
        prevActivity = new AtomicReference<>();
        devices = Collections.synchronizedList(new ArrayList<>());
        devicesId = Collections.synchronizedList(new ArrayList<>());

        new BackgroundTask(this).execute();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, devicesId);
        devicesSpinner.setAdapter(adapter);

        selectBtn.setOnClickListener(v -> {
            if(devicesSpinner.getSelectedItem() != null) {
                Log.d(TAG, LogMessages.CHANGE_HOME.label);
                try {
                    Intent intent = new Intent(getActivity(), Class.forName(prevActivity.get()));
                    intent.putExtra("deviceId", String.valueOf(devicesSpinner.getSelectedItem()));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "Device not selected");
                Toast.makeText(getActivity(), "Please select a device!", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> {
            Log.d(TAG, LogMessages.CHANGE_HOME.label);
            //TODO update
            ((DeviceSettingsActivity)getActivity()).setViewPager(0);
        });

        return view;
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceSelectFragment> weakReference;

        BackgroundTask(DeviceSelectFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DeviceSelectFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            DeviceSelectFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_WORKING.label);

            Bundle bundle = fragment.getArguments();
            if (bundle != null) {
                fragment.userId.set(bundle.getString("userId"));
                fragment.prevActivity.set(bundle.getString("prevActivity"));
            } else {
                Log.e(fragment.TAG, LogMessages.USER_FETCH_ERROR.label);
            }

            //TODO FIX
            fragment.firebaseHandler.getUserDevices(fragment.userId.get());
            fragment.devices.forEach(device -> fragment.devicesId.add(device.getId()));

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DeviceSelectFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
