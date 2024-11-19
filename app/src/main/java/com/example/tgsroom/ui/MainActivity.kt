package com.example.tgsroom.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tgsroom.R
import com.example.tgsroom.database.TrackDao
import com.example.tgsroom.database.TrackDatabase
import com.example.tgsroom.databinding.ActivityMainBinding
import com.example.tgsroom.model.ApiData
import com.example.tgsroom.network.ApiClient
import com.example.tgsroom.database.Trackk
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackDao: TrackDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackDao = TrackDatabase.getDatabase(applicationContext).trackDao()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        trackAdapter = TrackAdapter(emptyList()) { track ->
            toggleFavorite(track)
        }
        binding.recyclerView.adapter = trackAdapter

        getChartData()

        binding.favoriteIconButton.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getChartData() {
        val call = ApiClient.getInstance().getChartData()
        call.enqueue(object : Callback<ApiData> {
            override fun onResponse(call: Call<ApiData>, response: Response<ApiData>) {
                if (response.isSuccessful) {
                    val apiTracks = response.body()?.tracks?.data?.distinctBy { it.id }?.map {
                        Trackk(
                            trackId = it.id,
                            title = it.title,
                            artistName = it.artist.name,
                            albumCoverUrl = it.album.coverUrl
                        )
                    } ?: emptyList()

                    lifecycleScope.launch {
                        val favoriteTracks = trackDao.getFavoriteTracks().associateBy { it.trackId }
                        val tracks = apiTracks.map { track ->
                            track.apply {
                                favorite = favoriteTracks[track.trackId]?.favorite ?: 0
                            }
                        }

                        trackAdapter.updateData(tracks)
                        saveTracksToDatabase(tracks)
                    }
                } else {
                    Toast.makeText(applicationContext, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiData>, t: Throwable) {
                Toast.makeText(applicationContext, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveTracksToDatabase(tracks: List<Trackk>) {
        lifecycleScope.launch {
            tracks.forEach { track ->
                val existingTrack = trackDao.getTrackById(track.trackId)
                if (existingTrack == null) {
                    trackDao.insert(track)
                } else {
                    // Jika track sudah ada, perbarui status favorit
                    track.favorite = existingTrack.favorite
                    trackDao.updateTrack(track)
                }
            }
        }
    }

    private fun toggleFavorite(track: Trackk) {
        track.favorite = if (track.favorite == 1) 0 else 1
        lifecycleScope.launch {
            if (track.favorite == 1) {
                trackDao.markAsFavorite(track.trackId)
                Toast.makeText(applicationContext, "Favorit ditambahkan", Toast.LENGTH_SHORT).show()
            } else {
                trackDao.removeFromFavorites(track.trackId)
                Toast.makeText(applicationContext, "Favorit dibatalkan", Toast.LENGTH_SHORT).show()
            }
            trackAdapter.notifyDataSetChanged()
        }
    }
}