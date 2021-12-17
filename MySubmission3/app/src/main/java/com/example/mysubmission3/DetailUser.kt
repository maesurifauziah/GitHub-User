package com.example.mysubmission3

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mysubmission3.adapter.SectionsPagerAdapter
import com.example.mysubmission3.data.DataUser
import com.example.mysubmission3.data.Favorite
import com.example.mysubmission3.databinding.DetailUserBinding
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.AVATAR
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.COMPANY
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.FAVORITE
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.LOCATION
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.NAME
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.REPOSITORY
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.example.mysubmission3.db.FavoriteHelper
import kotlinx.android.synthetic.main.detail_user.*

class DetailUser : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding: DetailUserBinding

    private var isFavorite = false
    private lateinit var favHelper: FavoriteHelper
    private var favorites: Favorite? = null
    private lateinit var imageAvatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        favHelper = FavoriteHelper.getInstance(applicationContext)
        favHelper.open()

        favorites = intent.getParcelableExtra(EXTRA_NOTE)
        if (favorites != null) {
            setDataObject()
            isFavorite = true
            val checked: Int = R.drawable.ic_favorite
            fa_btn_favorite.setImageResource(checked)
        } else {
            setData()
        }

        viewPagerConfig()
        fa_btn_favorite.setOnClickListener(this)
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
        imageAvatar = dataUser.avatar.toString()
    }

    private fun setDataObject() {
        val favoriteUser = intent.getParcelableExtra<Favorite>(EXTRA_NOTE) as Favorite
        favoriteUser.name?.let { setActionBarTitle(it) }
        binding.detailName.text = favoriteUser.name
        binding.detailUsername.text = favoriteUser.username
        binding.detailCompany.text = favoriteUser.company
        binding.detailLocation.text = favoriteUser.location
        binding.detailRepository.text = favoriteUser.repository
        Glide.with(this)
            .load(favoriteUser.avatar)
            .into(binding.detailAvatar)
        imageAvatar = favoriteUser.avatar.toString()
    }

    override fun onClick(view: View) {
        val checked: Int = R.drawable.ic_favorite
        val unChecked: Int = R.drawable.ic_favorite_border
        if (view.id == R.id.fa_btn_favorite) {
            if (isFavorite) {
                favHelper.deleteById(favorites?.username.toString())
                Toast.makeText(this, getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show()
                fa_btn_favorite.setImageResource(unChecked)
                isFavorite = false
            } else {
                val dataUsername = detail_username.text.toString()
                val dataName = detail_name.text.toString()
                val dataAvatar = imageAvatar
                val dataCompany = detail_company.text.toString()
                val dataLocation = detail_location.text.toString()
                val dataRepository = detail_repository.text.toString()
                val dataFavorite = "1"

                val values = ContentValues()
                values.put(USERNAME, dataUsername)
                values.put(NAME, dataName)
                values.put(AVATAR, dataAvatar)
                values.put(COMPANY, dataCompany)
                values.put(LOCATION, dataLocation)
                values.put(REPOSITORY, dataRepository)
                values.put(FAVORITE, dataFavorite)

                isFavorite = true
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                fa_btn_favorite.setImageResource(checked)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
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