package com.gypsophila.androidpro;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Gypsophila on 2016/7/30.
 */
public class ImageFragment extends DialogFragment {

    public static final String EXTRA_IMAGE_PATH = "com.gypsophila.androidpro.image_path";
    public static final String EXTRA_IMAGE_ROTATE_DEGREES = "com.gypsophila.androidpro.image_rotate_degrees";
    private ImageView mImageView;

    public static ImageFragment newInstance(String imagePath, float degrees) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        bundle.putSerializable(EXTRA_IMAGE_ROTATE_DEGREES, degrees);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(bundle);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = (String) getArguments().getSerializable(EXTRA_IMAGE_PATH);
        float degrees = (float) getArguments().getSerializable(EXTRA_IMAGE_ROTATE_DEGREES);
        BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path,degrees);
        mImageView.setImageDrawable(image);
        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }
}
