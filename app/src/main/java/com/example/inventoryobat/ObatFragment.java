package com.example.inventoryobat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ObatFragment extends Fragment {

    private static final String ARG_JENIS = "jenis_obat";
    private String jenisObat;
    private RecyclerView rvObat;
    private ObatAdapter adapter;
    private MainViewModel viewModel;

    public static ObatFragment newInstance(String jenisObat) {
        ObatFragment fragment = new ObatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JENIS, jenisObat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jenisObat = getArguments().getString(ARG_JENIS);
        }
    }

    public void filterData(String keyword) {
        if (adapter != null) {
            adapter.filterObat(keyword);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obat, container, false);

        rvObat = view.findViewById(R.id.rvObat);
        rvObat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvObat.setHasFixedSize(true);

        viewModel = new MainViewModel(getContext());

        adapter = new ObatAdapter(getContext(), new ArrayList<>());
        rvObat.setAdapter(adapter);

        viewModel.getObatList().observe(getViewLifecycleOwner(), obatList -> {
            adapter.updateData(obatList);
        });

        if (jenisObat.equals("SEMUA")) {
            viewModel.loadAllObat();
        } else {
            viewModel.loadObatByJenis(jenisObat);
        }

        return view;
    }

}

