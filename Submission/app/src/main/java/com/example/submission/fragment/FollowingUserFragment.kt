package com.example.submission.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission.BuildConfig
import com.example.submission.R
import com.example.submission.UserGitDetailActivity
import com.example.submission.adapter.FollowingUserAdapter
import com.example.submission.data.FavoriteUserGit
import com.example.submission.data.UserGit
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers_user.*
import kotlinx.android.synthetic.main.fragment_following_user.*
import org.json.JSONArray
import org.json.JSONObject

class FollowingUserFragment : Fragment() {
    companion object {
        private val TAG = FollowingUserFragment::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
    }

    private var listUserGit: ArrayList<UserGit> = ArrayList()
    private lateinit var adapter: FollowingUserAdapter
    private var favoriteUserGit: FavoriteUserGit? = null
    private lateinit var dataFavorite: FavoriteUserGit
    private lateinit var dataUser: UserGit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowingUserAdapter(listUserGit)
        listUserGit.clear()
        favoriteUserGit = activity?.intent?.getParcelableExtra(UserGitDetailActivity.EXTRA_NOTE)
        if (favoriteUserGit != null) {
            dataFavorite = activity?.intent?.getParcelableExtra<FavoriteUserGit>(FollowersUserFragment.EXTRA_NOTE) as FavoriteUserGit
            getUserGitFollowing(dataFavorite.username.toString())
        } else {
            dataUser = activity?.intent?.getParcelableExtra<UserGit>(FollowersUserFragment.EXTRA_DATA) as UserGit
            getUserGitFollowing(dataUser.username.toString())
        }
    }

    private fun getUserGitFollowing(id: String) {
        statusLoading(true)
        val client = AsyncHttpClient()
        val token = BuildConfig.GITHUB_TOKEN
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
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
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
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
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
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
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
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
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun showRecyclerList() {
        recycleViewFollowing.layoutManager = LinearLayoutManager(activity)
        recycleViewFollowing.adapter = adapter
    }

    private fun statusLoading(status: Boolean) {
        if (status) {
            progressBarFollowing.visibility = View.VISIBLE
        } else {
            progressBarFollowing.visibility = View.INVISIBLE
        }
    }
}