package com.example.mysubmission1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class Detail : AppCompatActivity() {
    companion object {
        const val EXTRA_DEVELOPER = "extra_developer"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        val nama: TextView = findViewById(R.id.nama)
        val username: TextView = findViewById(R.id.username)
        val company: TextView = findViewById(R.id.company)
        val location: TextView = findViewById(R.id.location)
        val repository: TextView = findViewById(R.id.repository)
        val followers: TextView = findViewById(R.id.followers)
        val following: TextView = findViewById(R.id.following)
        val avatar: ImageView = findViewById(R.id.avatar)

        val developer = intent.getParcelableExtra<Developer>(EXTRA_DEVELOPER) as Developer

        val bundle: Bundle = intent.extras!!
        val photo: Int = bundle.getInt(EXTRA_AVATAR)
        avatar.setImageResource(photo)

        val nama_dev = developer.name
        val username_dev = developer.username
        val company_dev = developer.company
        val location_dev = developer.location
        val repository_dev = developer.repository
        val followers_dev = developer.followers
        val following_dev = developer.following

        nama.text = nama_dev
        username.text = username_dev
        company.text = company_dev
        location.text = location_dev
        repository.text = repository_dev
        followers.text = followers_dev
        following.text = following_dev

        val actionbar = supportActionBar
        actionbar!!.title = "Detail"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

