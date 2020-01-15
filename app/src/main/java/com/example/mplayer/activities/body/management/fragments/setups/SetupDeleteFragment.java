package com.example.mplayer.activities.body.management.fragments.setups;

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
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.activities.body.management.activities.ManageSetupActivity;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class SetupDeleteFragment extends Fragment {

    private static final String TAG = "SetupDeleteFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.i(TAG, "Setup delete fragment started");

        //TODO set element references
        final Spinner setupsSpinner = view.findViewById();
        final Button deleteSetupBtn = view.findViewById();
        final Button doneBtn = view.findViewById();

        firebaseHandler = FirebaseHandler.getInstance();

        final List<String> setupsId = new ArrayList<>();

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO update when firebase handler is done
                List<Setup> setups = firebaseHandler.getSetups();
                setupsId.clear();

                for(Setup setup : setups) {
                    setupsId.add(setup.getId());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, setupsId);
                setupsSpinner.setAdapter(adapter);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();



        deleteSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setupsSpinner.getSelectedItem() != null) {
                    Log.d(TAG, "Deleting setup with id:" + setupsSpinner.getSelectedItem());
                    firebaseHandler.deleteSetup(String.valueOf(setupsSpinner.getSelectedItemId()));
                } else {
                    Log.e(TAG, "Setup not selected");
                    Toast.makeText(getActivity(), "Please select a setup!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                Log.d(TAG, "Changing to setup home fragment");
                ((ManageSetupActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}
