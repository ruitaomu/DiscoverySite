package com.jfighter.discoverysite.ui.discovery;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jfighter.discoverysite.R;
import com.jfighter.discoverysite.databinding.FragmentDiscoveryBinding;

public class DiscoveryFragment extends Fragment {

    private FragmentDiscoveryBinding binding;

    protected RecyclerView mRecyclerView;
    protected DiscoveryListAdapter mAdapter;
    private DiscoveryViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DiscoveryViewModel discoveryViewModel =
                new ViewModelProvider(this).get(DiscoveryViewModel.class);

        binding = FragmentDiscoveryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new DiscoveryListAdapter(new DiscoveryListAdapter.WordDiff(), this);
        // Set DiscoveryListAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = new ViewModelProvider(getActivity()).get(DiscoveryViewModel.class);
        mViewModel.getAllWords().observe(getActivity(), words -> {
            // Update the cached copy of the words in the adapter.
            mAdapter.submitList(words);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void openWebView(String url) {
        PopupDetailsWindow popupWindow = new PopupDetailsWindow(getContext());
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {

    }
}