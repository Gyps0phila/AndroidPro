package com.gypsophila.androidpro;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gypsophila on 2016/7/3.
 */
public class CriminalIntentJSONSerializer {
    private String mFileName;
    private Context mContext;

    public CriminalIntentJSONSerializer(String mFileName, Context mContext) {
        this.mFileName = mFileName;
        this.mContext = mContext;
    }

    public void saveCrimes(List<Crime> crimes) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Crime crime : crimes) {
            array.put(crime.toJson());
        }
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public List<Crime> loadCrimes() throws IOException, JSONException {
        List<Crime> crimes = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStream is = mContext.openFileInput(mFileName);
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(sb.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return crimes;
    }
}
