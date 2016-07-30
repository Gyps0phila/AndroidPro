package com.gypsophila.androidpro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Gypsophila on 2016/6/10.
 */
public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    public static final String EXTRA_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_PHOTO = 1;
    private Button mTimeButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private static final String DIALOG_IMAGE = "image";

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        SimpleDateFormat sdf = new SimpleDateFormat("EE,yyyy-MM-dd");
        mDateButton.setText(sdf.format(mCrime.getmDate()));
//        mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
//                DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
        mTimeButton = (Button) view.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(fm, DIALOG_TIME);
            }
        });
        CheckBox mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        mPhotoButton = (ImageButton) view.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });
        mPhotoView = (ImageView) view.findViewById(R.id.crime_imageView);
        registerForContextMenu(mPhotoView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mCrime.getmPhoto();
                if (p == null) return;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
                ImageFragment.newInstance(path, p.getmRotateDegrees()).show(fm, DIALOG_IMAGE);
            }
        });
        PackageManager pm = getActivity().getPackageManager();
        boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || Camera.getNumberOfCameras() > 0;
        if (!hasCamera) {
            mPhotoButton.setEnabled(false);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CODE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            SimpleDateFormat sdf = new SimpleDateFormat("EE,yyyy-MM-dd");
            mDateButton.setText(sdf.format(mCrime.getmDate()));
        }
        if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            float degrees = data.getFloatExtra(CrimeCameraFragment.EXTRA_PHOTO_ROTATE_DEGREES, 0);
            if (filename != null) {
//                Log.i(TAG, "filename:" + filename);
                Photo photo = new Photo(filename, degrees);
                Photo oldPhoto = mCrime.getmPhoto();
                if (oldPhoto != null) {
                    File photoFile = new File(getActivity().getFileStreamPath(oldPhoto.getmFilename()).getAbsolutePath());
                    if (photoFile.isFile() && photoFile.exists()) {
                        photoFile.delete();
                    }
                }
                mCrime.setmPhoto(photo);
//                Log.i(TAG, "Crime:" + mCrime.getmTitle() + " has a photo");
                showPhoto();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_imageview_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab crimes = CrimeLab.getInstance(getActivity());
                crimes.deleteCrime(mCrime);
                crimes.saveCrimes();
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    private void showPhoto() {
        Photo p = mCrime.getmPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path, p.getmRotateDegrees());
        }
        mPhotoView.setImageDrawable(b);
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.crime_imageView:
                getActivity().getMenuInflater().inflate(R.menu.fragment_crime_imageview_menu, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_photo:
                deletePhoto();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deletePhoto() {
        Photo p = mCrime.getmPhoto();
        if (null == p) return;
        mPhotoView.setImageDrawable(null);
        File photoFile = new File(getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath());
        if (photoFile.isFile() && photoFile.exists()) {
            photoFile.delete();
            mCrime.setmPhoto(null);
        }
    }
}
