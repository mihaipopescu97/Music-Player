package com.example.mplayer.activities.body.management.fragments.setups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.activities.body.management.ManageDeviceActivity;
import com.example.mplayer.activities.body.management.ManageSetupActivity;
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
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.d(TAG, "Setup add fragment started");

        firebaseHandler = FirebaseHandler.getInstance();
        final List<Room> rooms = new ArrayList<>();

        //TODO set element references
        final TextView nrOfRooms = view.findViewById();
        final Button addSetupBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        //TODO pass device id;
        final String deviceId = "";

        addSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setup setup = new Setup(deviceId);

                //TODO check if int has been added
                int nr = Integer.parseInt(nrOfRooms.getText().toString());

                for(int i = 0; i < nr; i++) {
                    Room room = new Room(setup.getId());
                    rooms.add(room);
                }

                setup.setRooms(rooms);

                firebaseHandler.addSetup(setup);

                ((ManageDeviceActivity)getActivity()).setViewPager(0);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageSetupActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}
