package com.gypsophila.androidpro;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gypsophila on 2016/7/27.
 */
public class Photo {

    private static final String JSON_FILENAME = "filename";
    private static final String JSON_ROTATE_DEGREES = "rotate_degrees";
    private String mFilename;
    private float mRotateDegrees;

    public Photo(String filename) {
        mFilename = filename;
        mRotateDegrees = 0;
    }
    public Photo(String filename, float degrees) {
        mFilename = filename;
        mRotateDegrees = degrees;
    }

    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
        mRotateDegrees = json.getInt(JSON_ROTATE_DEGREES);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        json.put(JSON_ROTATE_DEGREES, mRotateDegrees);
        return json;
    }

    public String getmFilename() {
        return mFilename;
    }

    public float getmRotateDegrees() {
        return mRotateDegrees;
    }
}
