package com.example.mysubmission2

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mysubmission2.adapter.SectionsPagerAdapter
import com.example.mysubmission2.data.DataUser
import com.example.mysubmission2.databinding.DetailUserBinding
import kotlinx.android.synthetic.main.detail_user.*

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var binding: DetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()

        viewPagerConfig()
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    private fun setData() {
        val dataUser = intent.getParcelableExtra<DataUser>(EXTRA_DATA) as DataUser
        dataUser.name?.let { setActionBarTitle(it) }
        binding.detailName.text = dataUser.name
        binding.detailUsername.text = dataUser.username
        binding.detailCompany.text = getString(R.string.company, dataUser.company)
        binding.detailLocation.text = getString(R.string.location, dataUser.location)
        binding.detailRepository.text = getString(R.string.repository, dataUser.repository)
        Glide.with(this)
            .load(dataUser.avatar)
            .into(binding.detailAvatar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        if (item.itemId == R.id.change_theme_settings) {
            val mIntent = Intent(this, SettingsTheme::class.java)
            startActivity(mIntent)
        }
        if (item.itemId == R.id.action_favorite) {
            val mIntent = Intent(this, FavoriteUser::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

}

