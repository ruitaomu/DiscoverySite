package com.jfighter.discoverysite.ui.discovery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jfighter.discoverysite.R;
import com.jfighter.discoverysite.util.Helper;
import com.jfighter.discoverysite.util.PoiInfo;

import java.io.InputStream;

public class PopupDetailsWindow extends PopupWindow {

    public PopupDetailsWindow(Context context, String name) {
        super(context);

        // Set popup window size
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        // Set background drawable
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Set content view
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_details_window, null);
        setContentView(contentView);

        // Get TextView and ImageView from content view
        TextView detailsTitleTextView = contentView.findViewById(R.id.detailsTitleTextView);
        TextView textView = contentView.findViewById(R.id.detailsTextView);
        ImageView imageView = contentView.findViewById(R.id.detailsImageView);

        // Set text and image for TextView and ImageView here

        PoiInfo poi = Helper.POI().getPOIByName(name);
        if (poi != null) {
            String imageName;
            imageName = poi.getmImageName();
            int resourceId = context.getResources().
                    getIdentifier(imageName, "raw", context.getPackageName());
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);

            textView.setText(poi.getmDescription());
            detailsTitleTextView.setText(poi.getmSiteName());
        }

        // Get close button from content view and set click listener
        Button closeButton = contentView.findViewById(R.id.detailsCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
