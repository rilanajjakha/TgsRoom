package com.example.tgsroom.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tgsroom.databinding.ActivityFavoritesBinding
import com.example.tgsroom.database.TrackDao
import com.example.tgsroom.database.TrackDatabase
import com.example.tgsroom.database.Trackk
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var trackDao: TrackDao
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackDao = TrackDatabase.getDatabase(applicationContext).trackDao()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(emptyList()) { track ->
        }
        binding.recyclerView.adapter = trackAdapter

        getFavoriteTracks()
    }

    private fun getFavoriteTracks() {
        lifecycleScope.launch {
            val favoriteTracks = trackDao.getFavoriteTracks()
            trackAdapter.updateData(favoriteTracks.distinctBy {
                it.trackId
            })
        }
    }
}