package com.example.tgsroom.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tgsroom.R
import com.example.tgsroom.database.Trackk
import com.example.tgsroom.databinding.ItemTrackBinding

class TrackAdapter(
    private var tracks: List<Trackk>,
    private val onFavoriteClicked: (Trackk) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    fun updateData(newTracks: List<Trackk>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = tracks.size

    inner class TrackViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Trackk) {
            binding.trackTitle.text = track.title
            binding.artistName.text = track.artistName
            Glide.with(binding.albumCover.context)
                .load(track.albumCoverUrl)
                .into(binding.albumCover)

            binding.favoriteIcon.setImageResource(
                if (track.favorite == 1) R.drawable.baseline_favorite_24
                else R.drawable.outline_favorite_border_24
            )

            binding.favoriteIcon.setOnClickListener {
                onFavoriteClicked(track)

                binding.favoriteIcon.setImageResource(
                    if (track.favorite == 1) R.drawable.baseline_favorite_24
                    else R.drawable.outline_favorite_border_24
                )
            }
        }
    }
}