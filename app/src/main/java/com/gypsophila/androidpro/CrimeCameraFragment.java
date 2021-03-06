package com.gypsophila.androidpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gypsophila on 2016/7/24.
 */
public class CrimeCameraFragment extends Fragment {

    private static final String TAG = "CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME = "com.gypsophila.androidpro.photo_filename";
    public static final String EXTRA_PHOTO_ROTATE_DEGREES = "com.gypsophila.androidpro.photo_rotate_degrees";
    private Button mTakePictureButton;
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private View mProgressContainer;
    private float mPhotoRotateDegrees;
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String filename = UUID.randomUUID().toString() + ".jpg";
            FileOutputStream fos = null;
            boolean success = true;
            try {
                fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(data);
            } catch (Exception e) {
//                e.printStackTrace();
                Log.e(TAG, "Error Writing to file " + filename, e);
                success = false;
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    Log.e(TAG, "Error Closing file " + filename, e);
                    success = false;
                }
            }

            if (success) {
//                Log.i(TAG, "JPEG saved at " + filename);
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME, filename);
                i.putExtra(EXTRA_PHOTO_ROTATE_DEGREES, mPhotoRotateDegrees);
                getActivity().setResult(Activity.RESULT_OK, i);
            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };
    private SensorManager sensorManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surfaceView);
        mTakePictureButton = (Button) view.findViewById(R.id.crime_camera_takePictureButton);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }
            }
        });
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.
                TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, magneticSensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener, accelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        mProgressContainer = view.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                    Log.e(TAG, "error setting up preview display, so stop the preview");
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null) return;
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getSupprortedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(s.width, s.height);
                s = getSupprortedSize(parameters.getSupportedPictureSizes(), width, height);
                parameters.setPictureSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                } catch (Exception e) {
//                    e.printStackTrace();
                    Log.e(TAG, "could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }
        });
        return view;
    }

    private SensorEventListener listener = new SensorEventListener() {

        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            //判断当前是地磁传感器还是加速度传感器
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticValues = event.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
            SensorManager.getOrientation(R, values);
            //将旋转角度赋予具体photo
            mPhotoRotateDegrees = -(float) Math.toDegrees(values[0]);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private Camera.Size getSupprortedSize(List<Camera.Size> sizes, int w, int h) {
        Camera.Size bestSize = sizes.get(0);
        int largeArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largeArea) {
                largeArea = area;
                bestSize = s;
            }
        }
        return bestSize;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
