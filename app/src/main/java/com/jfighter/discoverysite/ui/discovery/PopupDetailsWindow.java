package com.jfighter.discoverysite.ui.discovery;

import android.content.Context;
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

public class PopupDetailsWindow extends PopupWindow {

    public PopupDetailsWindow(Context context) {
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
        TextView textView = contentView.findViewById(R.id.detailsTextView);
        ImageView imageView = contentView.findViewById(R.id.detailsImageView);

        // Set text and image for TextView and ImageView here
        textView.setText("Delphi神庙\n    阿波罗神殿");

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
