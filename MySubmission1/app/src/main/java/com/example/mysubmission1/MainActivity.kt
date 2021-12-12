package com.example.mysubmission1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvDeveloper: RecyclerView
    private val list = ArrayList<Developer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvDeveloper =  findViewById(R.id.rv_developer)
        rvDeveloper.setHasFixedSize(true)

        list.addAll(listDeveloper)
        showRecyclerList()
    }

    private val listDeveloper: ArrayList<Developer>
        get() {
            val dataName = resources.getStringArray(R.array.name)
            val dataUsername = resources.getStringArray(R.array.username)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)
            val dataFollowers = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)
            val listDeveloper = ArrayList<Developer>()
            for (i in dataName.indices) {
                val developer = Developer(dataName[i],dataUsername[i],dataCompany[i],dataLocation[i],dataRepository[i],dataFollowers[i],dataFollowing[i], dataAvatar.getResourceId(i, -1))
                listDeveloper.add(developer)
            }
            return listDeveloper
        }
    private fun showRecyclerList() {
        rvDeveloper.layoutManager = LinearLayoutManager(this)
        val listDeveloperAdapter = ListDeveloperAdapter(list)
        rvDeveloper.adapter = listDeveloperAdapter

        listDeveloperAdapter.setOnItemClickCallback(object : ListDeveloperAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Developer) {
                showSelectedDeveloper(data)
            }
        })
    }

    private fun showSelectedDeveloper(developer: Developer) {
        val detail = Intent(this@MainActivity, Detail::class.java)
        detail.putExtra (Detail.EXTRA_DEVELOPER, developer)
        detail.putExtra(Detail.EXTRA_AVATAR, developer.avatar)
        startActivity(detail)
    }
}