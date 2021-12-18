package com.example.submission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission.adapter.UserGitAdapter
import com.example.submission.data.UserGit
import com.example.submission.databinding.ActivityMainBinding
import com.example.submission.databinding.ActivitySettingsThemeBinding
import com.example.submission.model.MainViewModel
import com.example.submission.model.ViewModelFactory
import com.example.submission.setting.SettingPreferences
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var listUserGit: ArrayList<UserGit> = ArrayList()
    private lateinit var adapter: UserGitAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSetting: ActivitySettingsThemeBinding

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.app_name)

        adapter = UserGitAdapter(listUserGit)

        statusLoading(true)

        getUserGit()
        searchUserGit()
        recyclerViewConfig()
        getThemeSettings()
    }

    private fun getUserGit() {
        statusLoading(true)
        val client = AsyncHttpClient()
        val token = BuildConfig.GITHUB_TOKEN
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray) {
                statusLoading(false)
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserGitDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                statusLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getUserGitDetail(id: String) {
        statusLoading(true)
        val client = AsyncHttpClient()
        val token = BuildConfig.GITHUB_TOKEN
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                statusLoading(false)
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username: String = jsonObject.getString("login").toString()
                    val name: String = jsonObject.getString("name").toString()
                    val avatar: String = jsonObject.getString("avatar_url").toString()
                    val company: String = jsonObject.getString("company").toString()
                    val location: String = jsonObject.getString("location").toString()
                    val repository: Int = jsonObject.getInt("public_repos")
                    val followers: Int = jsonObject.getInt("followers")
                    val following: Int = jsonObject.getInt("following")
                    listUserGit.add(
                        UserGit(
                            username,
                            name,
                            avatar,
                            company,
                            location,
                            repository,
                            followers,
                            following
                        )
                    )
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                statusLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun showRecyclerList() {
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = UserGitAdapter(listUserGit)
        binding.recycleView.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object : UserGitAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUsers: UserGit) {
                showSelectedUserGit(dataUsers)
            }
        })
    }

    private fun showSelectedUserGit(dataUser: UserGit) {
        UserGit(
            dataUser.username,
            dataUser.name,
            dataUser.avatar,
            dataUser.company,
            dataUser.location,
            dataUser.repository,
            dataUser.followers,
            dataUser.following
        )
        val intent = Intent(this@MainActivity, UserGitDetailActivity::class.java)
        intent.putExtra(UserGitDetailActivity.EXTRA_DATA, dataUser)

        this@MainActivity.startActivity(intent)
        Toast.makeText(
            this,
            "${dataUser.name}",
            Toast.LENGTH_SHORT
        ).show()

    }

    private fun searchUserGit() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    listUserGit.clear()
                    getUserGitSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }


    private fun getUserGitSearch(id: String) {
        statusLoading(true)
        val client = AsyncHttpClient()
        val token = BuildConfig.GITHUB_TOKEN
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                statusLoading(false)
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserGitDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                statusLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun recyclerViewConfig() {
        binding.recycleView.layoutManager = LinearLayoutManager(recycleView.context)
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.addItemDecoration(
            DividerItemDecoration(
                binding.recycleView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_show, menu)
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
            R.id.action_favorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun statusLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun getThemeSettings() {
        bindingSetting = ActivitySettingsThemeBinding.inflate(layoutInflater)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    bindingSetting.switchThemeSetting.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    bindingSetting.switchThemeSetting.isChecked = false
                }
            })
    }

}