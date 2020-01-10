package com.example.mplayer.activities;

import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

public class SetupFragment extends Fragment {
    private static final String  TAG= "SetupFragment";

    private Spinner setups;
    private Button nextBtn;


}

    private void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
