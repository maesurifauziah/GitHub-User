package com.example.submission

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.submission.adapter.SectionsPagerAdapter
import com.example.submission.data.FavoriteUserGit
import com.example.submission.data.UserGit
import com.example.submission.databinding.ActivityUserGitDetailBinding
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.AVATAR
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.NAME
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.USERNAME
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.COMPANY
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.CONTENT_URI
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.LOCATION
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.REPOSITORY
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.FOLLOWERS
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.FOLLOWING
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.FAVORITE
import com.example.submission.db.FavoriteUserGitHelper
import kotlinx.android.synthetic.main.activity_user_git_detail.*

class UserGitDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding: ActivityUserGitDetailBinding
    private lateinit var favoriteUserGitHelper: FavoriteUserGitHelper
    private lateinit var imageAvatar: String
    private var favorites: FavoriteUserGit? = null
    private var liked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserGitDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteUserGitHelper = FavoriteUserGitHelper.getInstance(applicationContext)
        favoriteUserGitHelper.open()

        favorites = intent.getParcelableExtra(EXTRA_NOTE)
        if (favorites != null) {
            setDataDetailObject()
            liked = true
            val checked: Int = R.drawable.ic_baseline_favorite_24_red
            btn_favorite.setImageResource(checked)
        } else {
            setDataDetail()
        }

        viewPagerConfig()
        btn_favorite.setOnClickListener(this)
    }

    private fun setDataDetail() {
        val favoriteUser = intent.getParcelableExtra<UserGit>(EXTRA_DATA) as UserGit
        favoriteUser.name?.let { setActionBarTitle(it) }
        binding.dtlName.text = favoriteUser.name
        binding.dtlUsername.text = favoriteUser.username
        binding.dtlCompany.text = favoriteUser.company
        binding.dtlLocation.text = favoriteUser.location
        binding.dtlRepository.text = getString(R.string.label_repository, favoriteUser.repository.toString())
        binding.dtlFollowers.text = getString(R.string.label_followers, favoriteUser.followers.toString())
        binding.dtlFollowing.text = getString(R.string.label_following, favoriteUser.following.toString())
        Glide.with(this)
            .load(favoriteUser.avatar)
            .into(binding.dtlAvatar)
        imageAvatar = favoriteUser.avatar.toString()
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_show2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.settings_theme -> {
                val mIntent = Intent(this, SettingsThemeActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setDataDetailObject() {
        val favoriteUser = intent.getParcelableExtra<UserGit>(EXTRA_NOTE) as FavoriteUserGit
        favoriteUser.name?.let { setActionBarTitle(it) }
        binding.dtlName.text = favoriteUser.name
        binding.dtlUsername.text = favoriteUser.username
        binding.dtlCompany.text = favoriteUser.company
        binding.dtlLocation.text = favoriteUser.location
        binding.dtlRepository.text = getString(R.string.label_repository, favoriteUser.repository.toString())
        binding.dtlFollowers.text = getString(R.string.label_followers, favoriteUser.followers.toString())
        binding.dtlFollowing.text = getString(R.string.label_following, favoriteUser.following.toString())
        Glide.with(this)
            .load(favoriteUser.avatar)
            .into(binding.dtlAvatar)
        imageAvatar = favoriteUser.avatar.toString()
    }
    override fun onClick(view: View) {
        val checked: Int = R.drawable.ic_baseline_favorite_24_red
        val unChecked: Int = R.drawable.ic_baseline_favorite_border_24
        if (view.id == R.id.btn_favorite) {
            if (liked) {
                favoriteUserGitHelper.deleteById(favorites?.username.toString())
                Toast.makeText(this, getString(R.string.label_deleted_favorite), Toast.LENGTH_SHORT).show()
                btn_favorite.setImageResource(unChecked)
                liked = false
            } else {
                val dataUsername = dtl_username.text.toString()
                val dataName = dtl_name.text.toString()
                val dataAvatar = imageAvatar
                val dataCompany = dtl_company.text.toString()
                val dataLocation = dtl_location.text.toString()
                val dataRepository = dtl_repository.text.toString()
                val dataFollowers = dtl_followers.text.toString()
                val dataFollowing = dtl_following.text.toString()
                val dataFavorite = "1"

                val values = ContentValues()
                values.put(USERNAME, dataUsername)
                values.put(NAME, dataName)
                values.put(AVATAR, dataAvatar)
                values.put(COMPANY, dataCompany)
                values.put(LOCATION, dataLocation)
                values.put(REPOSITORY, dataRepository)
                values.put(FOLLOWERS, dataFollowers)
                values.put(FOLLOWING, dataFollowing)
                values.put(FAVORITE, dataFavorite)

                liked = true
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.label_add_favorite), Toast.LENGTH_SHORT).show()
                btn_favorite.setImageResource(checked)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        favoriteUserGitHelper.close()
    }
}