package com.gypsophila.androidpro;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gypsophila on 2016/6/20.
 */
public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private CriminalIntentJSONSerializer mSerializer;
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private List<Crime> mCrimes;
    private CrimeLab(Context ctx) {
        mAppContext = ctx;
        mCrimes = new ArrayList<>();
        mSerializer = new CriminalIntentJSONSerializer(FILENAME,mAppContext);
    }

    public static CrimeLab getInstance(Context ctx) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(ctx.getApplicationContext());
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime c : mCrimes) {
            if (uuid.equals(c.getmId())) {
                return c;
            }
        }
        return null;
    }

    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.i(TAG, "crimes saved to file");
            return true;
        }catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }
}
