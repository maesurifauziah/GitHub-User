package com.example.mysubmission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FavoriteUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)
//        supportActionBar?.title = "Favorite"
        title = getString(R.string.favorite)
    }
}