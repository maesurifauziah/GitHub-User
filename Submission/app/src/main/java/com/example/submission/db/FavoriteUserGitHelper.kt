package com.example.submission.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.TABLE_NAME
import com.example.submission.db.DatabaseContractFavorite.FavoriteColumns.Companion.USERNAME

class FavoriteUserGitHelper(context: Context) {
    private var databaseHelperFavorite: DatabaseHelperFavorite = DatabaseHelperFavorite(context)
    private var databaseFavorite: SQLiteDatabase = databaseHelperFavorite.writableDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteUserGitHelper? = null
        fun getInstance(context: Context): FavoriteUserGitHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteUserGitHelper(context)
        }
    }

    // get access to write databaseFavorite
    @Throws(SQLException::class)
    fun open() {
        databaseFavorite = databaseHelperFavorite.writableDatabase
    }

    fun close() {
        databaseHelperFavorite.close()
        if (databaseFavorite.isOpen)
            databaseFavorite.close()
    }

    fun queryAll(): Cursor {
        return databaseFavorite.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$USERNAME ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return databaseFavorite.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return databaseFavorite.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return databaseFavorite.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return databaseFavorite.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }
}