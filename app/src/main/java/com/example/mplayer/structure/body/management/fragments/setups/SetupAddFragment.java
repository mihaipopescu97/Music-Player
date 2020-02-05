package com.example.mplayer.structure.body.management.fragments.setups;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.SingleActivity;
import com.example.mplayer.structure.body.management.activities.ManageDeviceActivity;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class SetupAddFragment extends Fragment {

    private static final String TAG = "SetupAddFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_add, container, false);

        Log.i(TAG, "Setup add fragment started");

        firebaseHandler = FirebaseHandler.getInstance();

        final List<Room> rooms = new ArrayList<>();

        final EditText nrOfRooms = view.findViewById(R.id.setupAddId);
        final Button addSetupBtn = view.findViewById(R.id.setupAddBtn);
        final Button backBtn = view.findViewById(R.id.setupAddBackBtn);

        String deviceId = null;
        if(getArguments() != null) {
            deviceId = getArguments().getString("deviceId");
        } else {
            Log.e(TAG, "Device id not received");
            startActivity(new Intent(getActivity(), SingleActivity.class));
        }

        final String finalDeviceId = deviceId;
        addSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setup setup = new Setup(finalDeviceId);
                String text = nrOfRooms.getText().toString();
                try {
                    int nr = Integer.parseInt(text);
                    for(int i = 0; i < nr; i++) {
                        Room room = new Room(setup.getId());
                        rooms.add(room);
                    }

                    setup.setRooms(rooms);

                    Log.d(TAG, "Setup added with id:" + setup.getId());
                    firebaseHandler.addSetup(setup);

                    if(getActivity() != null) {
                        Log.i(TAG, "Changing to setup home fragment");
                        ((ManageDeviceActivity)getActivity()).setViewPager(0);
                    } else {
                        Log.e(TAG, "Activity is null");
                    }

                } catch (NumberFormatException e) {
                    Log.w(TAG, "Invalid number");
                    e.printStackTrace();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.i(TAG, "Changing to setup home fragment");
                    ((ManageDeviceActivity)getActivity()).setViewPager(0);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        return view;
    }
}
