package com.santrikoding.newsapp.data.local.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysArticle>)

    @Query("SELECT * FROM remote_keys_article WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeysArticle?

    @Query("DELETE FROM remote_keys_article")
    suspend fun deleteRemoteKeys()
}