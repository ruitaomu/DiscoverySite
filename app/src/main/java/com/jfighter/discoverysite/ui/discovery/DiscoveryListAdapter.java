package com.jfighter.discoverysite.ui.discovery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jfighter.discoverysite.R;
import com.jfighter.discoverysite.database.DiscoveryItem;
import com.jfighter.discoverysite.util.Helper;
import com.jfighter.discoverysite.util.PoiInfo;

public class DiscoveryListAdapter extends ListAdapter<DiscoveryItem, DiscoveryListAdapter.ViewHolder> {
    private static final String TAG = "DiscoveryListAdapter";

    private final DiscoveryFragment mParentFragment;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView poiImageView;

        public ViewHolder(View v, DiscoveryFragment parent) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    Log.d(TAG, "Item " + getTextView().getText() + " clicked.");
                    parent.openWebView(getTextView().getText().toString());
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
            poiImageView = (ImageView) v.findViewById(R.id.iconPoiView);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getPoiImageView() {
            return poiImageView;
        }
    }

    static class WordDiff extends DiffUtil.ItemCallback<DiscoveryItem> {

        @Override
        public boolean areItemsTheSame(@NonNull DiscoveryItem oldItem, @NonNull DiscoveryItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DiscoveryItem oldItem, @NonNull DiscoveryItem newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }


    public DiscoveryListAdapter(@NonNull DiffUtil.ItemCallback<DiscoveryItem> diffCallback, DiscoveryFragment parentFragment) {
        super(diffCallback);
        mParentFragment = parentFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.discovery_list_row_item, viewGroup, false);

        return new ViewHolder(v, mParentFragment);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        DiscoveryItem item = getItem(position);
        String name = item.getName();
        Log.d(TAG, "Item " + name);

        // Set ViewHolder's text
        viewHolder.getTextView().setText(name);

        // Set ViewHolder's image
        PoiInfo poi = Helper.POI().getPOIByName(name);
        if (poi != null) {
//            String imageName = poi.getmImageName();
//            Context context = viewHolder.getPoiImageView().getContext();
//            int resourceId = context.getResources().
//                    getIdentifier(imageName, "raw", context.getPackageName());

            int resourceId;
            switch (poi.getmType()) {
                case "statue":
                    resourceId = R.drawable.statue_icon;
                    break;
                case "arch":
                    resourceId = R.drawable.temple_icon;
                    break;
                default:
                    resourceId = R.drawable.questionmark_icon;
            }
            viewHolder.getPoiImageView().setImageResource(resourceId);
        }
    }
}
