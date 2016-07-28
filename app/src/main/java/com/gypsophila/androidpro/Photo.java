package com.gypsophila.androidpro;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gypsophila on 2016/7/27.
 */
public class Photo {

    private static final String JSON_FILENAME = "filename";
    private String mFilename;

    public Photo(String filename) {
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getmFilename() {
        return mFilename;
    }
}
