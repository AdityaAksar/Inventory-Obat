package com.example.inventoryobat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryobat.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new MainViewModel(this);

        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this);
        }

        setupViewPager();

        binding.fabAddObat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TambahObatActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(ObatFragment.newInstance("SEMUA"), "Semua");

        for (JenisObat jenis : JenisObat.values()) {
            adapter.addFragment(
                    ObatFragment.newInstance(jenis.name()),
                    jenis.getDisplayName()
            );
        }

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Cari nama obat...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCurrentFragment(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterCurrentFragment(newText);
                return true;
            }
        });
        return true;
    }

    private void filterCurrentFragment(String keyword) {
        int pos = binding.viewPager.getCurrentItem();
        ObatFragment fragment = (ObatFragment) ((ViewPagerAdapter)binding.viewPager.getAdapter())
                .getItem(pos);
        fragment.filterData(keyword);
    }


}