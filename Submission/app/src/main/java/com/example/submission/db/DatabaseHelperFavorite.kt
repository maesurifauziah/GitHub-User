package com.example.submission.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelperFavorite(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fav_user_git_db"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContractFavorite.FavoriteColumns.USERNAME} TEXT PRIMARY KEY  NOT NULL," +
                " ${DatabaseContractFavorite.FavoriteColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContractFavorite.FavoriteColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContractFavorite.FavoriteColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseContractFavorite.FavoriteColumns.LOCATION} TEXT NOT NULL," +
                " ${DatabaseContractFavorite.FavoriteColumns.REPOSITORY} TEXT NOT NULL," +
                " ${DatabaseContractFavorite.FavoriteColumns.FOLLOWERS} INTEGER NOT NULL," +
                " ${DatabaseContractFavorite.FavoriteColumns.FOLLOWING} INTEGER NOT NULL," +
                " ${DatabaseContractFavorite.FavoriteColumns.FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}