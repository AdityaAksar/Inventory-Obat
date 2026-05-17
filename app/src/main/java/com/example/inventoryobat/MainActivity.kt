package com.example.inventoryobat

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inventoryobat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = MainViewModel()

        if (!PermissionHelper.checkPermission(this)) {
            PermissionHelper.requestPermission(this)
        }

        setupViewPager()
        setupSearchListener()

        viewModel.errorMessage.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.fabAddObat.setOnClickListener {
            startActivity(Intent(this, TambahObatActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun setupSearchListener() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCurrentFragment(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(ObatFragment.newInstance("SEMUA"), "Semua")

        for (jenis in JenisObat.entries) {
            adapter.addFragment(
                ObatFragment.newInstance(jenis.displayName),
                jenis.displayName
            )
        }

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        val tabStrip = binding.tabLayout.getChildAt(0) as? ViewGroup
        tabStrip?.let { strip ->
            for (i in 0 until strip.childCount) {
                val tab = strip.getChildAt(i)
                val params = tab.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins(0, 0, 16, 0)
                tab.requestLayout()
            }
        }
    }

    private fun filterCurrentFragment(keyword: String) {
        val pos = binding.viewPager.currentItem
        val fragment = adapter.getItem(pos) as? ObatFragment
        fragment?.filterData(keyword)
    }
}
