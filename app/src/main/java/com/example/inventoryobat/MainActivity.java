package com.example.inventoryobat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryobat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        viewModel = new MainViewModel();

        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this);
        }

        setupViewPager();
        setupSearchListener();

        viewModel.getErrorMessage().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        binding.fabAddObat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TambahObatActivity.class);
            startActivity(intent);
        });

        binding.btnLogout.setOnClickListener(v -> {
            viewModel.logout();

            Toast.makeText(MainActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupSearchListener() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCurrentFragment(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(ObatFragment.newInstance("SEMUA"), "Semua");

        for (JenisObat jenis : JenisObat.values()) {
            adapter.addFragment(
                    ObatFragment.newInstance(jenis.getDisplayName()),
                    jenis.getDisplayName()
            );
        }

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) binding.tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 16, 0);
            tab.requestLayout();
        }
    }

    private void filterCurrentFragment(String keyword) {
        int pos = binding.viewPager.getCurrentItem();
        ObatFragment fragment = (ObatFragment) adapter.getItem(pos);
        if (fragment != null) {
            fragment.filterData(keyword);
        }
    }
}