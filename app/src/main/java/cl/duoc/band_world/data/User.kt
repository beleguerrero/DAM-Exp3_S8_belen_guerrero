package cl.duoc.band_world.data

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val username: String,
    val email: String,
    val password: String,
)

data class UserBandFavorite(
    val user_id: String,
    val band_id: String
)

data class UserGenreFavorite(
    val user_id: String,
    val genre_id: String
)

data class UserArtistFavorite(
    val user_id: String,
    val artist_id: String
)

data class UserAlbumFavorite(
    val user_id: String,
    val album_id: String
)

data class UserSongFavorite(
    val user_id: String,
    val song_id: String
)

