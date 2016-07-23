package com.gypsophila.androidpro;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Gypsophila on 2016/6/20.
 */
public class CrimeListActivity extends SimpleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }



}
