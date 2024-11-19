package com.example.tgsroom.model

import com.google.gson.annotations.SerializedName

data class ApiData(
    @SerializedName("tracks") val tracks: Tracks
)

data class Tracks(
    @SerializedName("data") val data: List<Track>
)

data class Track(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: Artist,
    @SerializedName("album") val album: Album
)

data class Artist(
    @SerializedName("name") val name: String
)

data class Album(
    @SerializedName("cover_medium") val coverUrl: String,
)