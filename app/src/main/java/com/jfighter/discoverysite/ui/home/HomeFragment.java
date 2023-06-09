package com.jfighter.discoverysite.ui.home;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jfighter.discoverysite.R;
import com.jfighter.discoverysite.databinding.FragmentHomeBinding;
import com.jfighter.discoverysite.util.Helper;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Context mContext;
    private LocationListener mLocationListener;
    private SensorEventListener mRotationListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // 注册 LocationListener
        mLocationListener = Helper.registerLocationUpdateListener(this, mContext, homeViewModel);
        mRotationListener = Helper.registerRotationUpdateListener(this, mContext, homeViewModel);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView arrowView = binding.homeCompassArrowView;
        arrowView.setImageResource(R.drawable.arrow);
        final ImageView bgView = binding.homeBackgroundView;
        bgView.setImageAlpha(64);
        bgView.setImageResource(R.drawable.homecompassbg);

        final TextView textView = binding.textView;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Create the observer which updates the UI.
        final Observer<Float> bearingObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float newBearing) {
                if (newBearing != null) {
                    arrowView.setRotation(newBearing);
                }
            }
        };
        homeViewModel.getBearing().observe(getViewLifecycleOwner(), bearingObserver);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Helper.removeLocationUpdater(mContext, mLocationListener);
        Helper.removeRotationUpdater(mContext, mRotationListener);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}