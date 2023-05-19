package com.jfighter.discoverysite.ui.radar;

import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jfighter.discoverysite.databinding.FragmentRadarBinding;
import com.jfighter.discoverysite.util.Helper;

public class RadarFragment extends Fragment {

    private FragmentRadarBinding binding;
    private Context mContext;
    private LocationListener mLocationListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RadarViewModel radarViewModel =
                new ViewModelProvider(this).get(RadarViewModel.class);

        // 注册 LocationListener
        mLocationListener = Helper.registerLocationUpdateListener(this, mContext, radarViewModel);

        binding = FragmentRadarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        radarViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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