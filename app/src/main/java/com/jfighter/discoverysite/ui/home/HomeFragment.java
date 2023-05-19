package com.jfighter.discoverysite.ui.home;

import android.content.Context;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // 注册 LocationListener
        mLocationListener = Helper.registerLocationUpdateListener(this, mContext, homeViewModel);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView imageView = binding.imageView;
        imageView.setImageResource(R.drawable.arrow);

        final TextView textView = binding.textView;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Create the observer which updates the UI.
        final Observer<Float> bearingObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float newBearing) {
                if (newBearing != null) {
                    imageView.setRotation(newBearing);
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}