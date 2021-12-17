package com.example.submission.helper

import android.database.Cursor
import com.example.submission.data.FavoriteUserGit
import com.example.submission.db.DatabaseContractFavorite
import java.util.*

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<FavoriteUserGit> {
        val favoriteList = ArrayList<FavoriteUserGit>()

        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.LOCATION))
                val repository = getInt(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.REPOSITORY))
                val followers = getInt(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.FOLLOWING))
                val favourite =
                    getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.FAVORITE))
                favoriteList.add(
                    FavoriteUserGit(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        following,
                        favourite
                    )
                )
            }
        }
        return favoriteList
    }
}