package com.gypsophila.androidpro;

import android.support.v4.app.Fragment;

/**
 * Created by Gypsophila on 2016/7/24.
 */
public class CrimeCameraActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
