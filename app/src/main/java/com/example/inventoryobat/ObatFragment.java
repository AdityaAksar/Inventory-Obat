package com.example.inventoryobat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.inventoryobat.databinding.FragmentObatBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ObatFragment extends Fragment {
    private FragmentObatBinding binding;
    private ObatAdapter adapter;
    private MainViewModel viewModel;
    private String jenisFilter;

    public static ObatFragment newInstance(String jenis) {
        ObatFragment fragment = new ObatFragment();
        Bundle args = new Bundle();
        args.putString("jenis", jenis);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentObatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvObat.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new MainViewModel();

        if (getArguments() != null) {
            jenisFilter = getArguments().getString("jenis");
        }

        adapter = new ObatAdapter(getContext(), new ArrayList<>());
        binding.rvObat.setAdapter(adapter);

        viewModel.getObatList().observe(getViewLifecycleOwner(), obatList -> {
            if (obatList != null) {
                adapter.updateData(obatList);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
    private void loadData() {
        if ("SEMUA".equals(jenisFilter)) {
            viewModel.loadAllObat();
        } else if (jenisFilter != null && !jenisFilter.isEmpty()) {
            viewModel.loadObatByJenis(jenisFilter);
        } else {
            viewModel.loadAllObat();
        }
    }

    public void filterData(String keyword) {
        if (adapter != null) {
            adapter.filterObat(keyword);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
