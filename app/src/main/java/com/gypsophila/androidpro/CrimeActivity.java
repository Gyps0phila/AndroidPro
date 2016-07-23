package com.gypsophila.androidpro;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}
