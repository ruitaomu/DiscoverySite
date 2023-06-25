package com.jfighter.discoverysite.ui.radar;

import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jfighter.discoverysite.R;
import com.jfighter.discoverysite.databinding.FragmentRadarBinding;
import com.jfighter.discoverysite.ui.discovery.PopupDetailsWindow;
import com.jfighter.discoverysite.util.Helper;

public class RadarFragment extends Fragment {

    private static final String TAG = "RadarFragment";
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

        final ImageView imageView = binding.imageViewRadar;
        imageView.setImageResource(R.drawable.radar);
        final ImageView imageViewForeground = binding.imageViewRadarForeground;
        imageViewForeground.setImageResource(R.drawable.radar_foreground);
        imageViewForeground.setImageAlpha(0);

        final TextView textView = binding.textRadardisp;
        radarViewModel.getText().observe(getViewLifecycleOwner(), text -> {
            try {
                Log.d(TAG, "Text change observed: "+text);
                int distance = (int)Double.parseDouble(text);
                textView.setText(text);
                int alpha = 255 - ((distance - 10) * 5);
                if (alpha < 0) alpha = 0;
                if (alpha > 255) alpha = 255;
                imageViewForeground.setImageAlpha(alpha);
            } catch (NumberFormatException e) {
                if (text.equals("No distance data")) {
                    textView.setText(text);
                } else {
                    PopupDetailsWindow popupWindow = new PopupDetailsWindow(getContext(), text);
                    popupWindow.setAnimationStyle(R.style.popupWindowAnimation);
                    popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
                }
            }
        });
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