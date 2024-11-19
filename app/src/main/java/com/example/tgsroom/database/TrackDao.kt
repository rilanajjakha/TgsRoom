package com.example.tgsroom.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: Trackk)

    @Delete
    suspend fun deleteTrack(track: Trackk)

    @Query("SELECT * FROM track_table")
    suspend fun getAllTracks(): List<Trackk>

    @Query("SELECT * FROM track_table WHERE track_id = :trackId LIMIT 1")
    suspend fun getTrackById(trackId: Long): Trackk?

    @Query("SELECT * FROM track_table WHERE favorite = 1")
    suspend fun getFavoriteTracks(): List<Trackk>

    @Update
    suspend fun updateTrack(track: Trackk)

    @Query("UPDATE track_table SET favorite = 1 WHERE track_id = :trackId")
    suspend fun markAsFavorite(trackId: Long)

    @Query("UPDATE track_table SET favorite = 0 WHERE track_id = :trackId")
    suspend fun removeFromFavorites(trackId: Long)
}