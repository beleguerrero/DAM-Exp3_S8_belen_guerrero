package cl.duoc.band_world.functions

import android.content.Context
import cl.duoc.band_world.data.Album
import cl.duoc.band_world.data.Artist
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.Genre
import cl.duoc.band_world.data.Song
import cl.duoc.band_world.data.User
import cl.duoc.band_world.data.UserAlbumFavorite
import cl.duoc.band_world.data.UserArtistFavorite
import cl.duoc.band_world.data.UserBandFavorite
import cl.duoc.band_world.data.UserGenreFavorite
import cl.duoc.band_world.data.UserSongFavorite
import cl.duoc.band_world.data.dao.BandDao
import cl.duoc.band_world.data.dao.UserDao
import cl.duoc.band_world.data.entities.UserAlbumFavoriteEntity
import cl.duoc.band_world.data.entities.UserArtistFavoriteEntity
import cl.duoc.band_world.data.entities.UserBandFavoriteEntity
import cl.duoc.band_world.data.entities.UserGenreFavoriteEntity
import cl.duoc.band_world.data.entities.UserSongFavoriteEntity
import cl.duoc.band_world.data.toDoamin
import cl.duoc.band_world.data.toDomain
import cl.duoc.band_world.data.toEntity
import cl.duoc.band_world.ui.components.FavoriteType

data class SongBandName(
    val song: Song,
    val bandName: String?
)

class UserFunctions(
    private val userDao: UserDao
) {

    suspend fun getAllUsers(): List<User>{
        val users = userDao.getAllUsers()
        return users.map {
            it.toDomain()
        }
    }


    suspend fun getUserById(userId: String): User? {
        val user = userDao.getUserById(userId)
        return user?.toDomain()
    }


    suspend fun getUserByUsername(username: String): User? {
        val user = userDao.getUserByUsername(username)
        return user?.toDomain()
    }


    suspend fun getUserByEmail(email: String): User? {
        val user = userDao.getUserByEmail(email)
        return user?.toDomain()
    }


    suspend fun getUserByUsernamePass(username: String, password: String): User? {
        val user = userDao.getUserByUsernamePass(username, password)
        return user?.toDomain()
    }

    suspend fun setUser(
        userId: String,
        name: String? = null,
        username: String? = null,
        email: String? = null,
        password: String? = null
    ): User? {
        val up = userDao.setUser(userId, name, username, email, password)

        if(up == 0) {
            return null
        }
        return userDao.getUserById(userId)?.toDomain()
    }


    suspend fun doUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): User? {
        val user = User(
            name = name,
            username = username,
            email = email,
            password = password
        )
        val insert = userDao.doUser(user.toEntity())
        if(insert == -1L) {
            return null
        }
        return user
    }




}



class FavoriteFunctions(
    private val bandDao: BandDao,
    private val userDao: UserDao
) {

    suspend fun getUserFavoriteBands(context: Context, userId: String, limit: Int? = null): List<Band> {
        val bands = if(limit != null) {
            userDao.getUserFavoriteBands(userId, limit)
        } else {
            userDao.getUserFavoriteBands(userId)
        }

        return bands.map {
            it.toDomain(context, null, null, null)
        }
    }

    suspend fun getUserFavoriteArtists(context: Context, userId: String, limit: Int? = null): List<Artist> {
        val artists = if(limit != null) {
            userDao.getUserFavoriteArtists(userId, limit)
        } else {
            userDao.getUserFavoriteArtists(userId)
        }
        return artists.map {
            it.toDomain(context)
        }
    }

    suspend fun getUserFavoriteGenres(userId: String, limit: Int? = null): List<Genre> {
        val genres = if(limit != null) {
            userDao.getUserFavoriteGenres(userId, limit)
        } else {
            userDao.getUserFavoriteGenres(userId)
        }
        return genres.map {
            it.toDomain()
        }
    }



    suspend fun getUserFavoriteAlbums(context: Context, userId: String, limit: Int? = null): List<Album> {
        val albums = if(limit != null) {
            userDao.getUserFavoriteAlbums(userId, limit)
        } else {
            userDao.getUserFavoriteAlbums(userId)
        }
        return albums.map {
            it.toDomain(context)
        }
    }



    suspend fun getUserFavoriteSongs(context: Context, userId: String, limit: Int? = null): List<SongBandName> {
        val songs = if(limit != null) {
            userDao.getUserFavoriteSongs(userId, limit)
        } else {
            userDao.getUserFavoriteSongs(userId)
        }
        return songs.map {
            SongBandName(
                song = it.toDomain(context),
                bandName = bandDao.getBandNameBySong(it.song.id)
            )
        }
    }

    suspend fun getTopArtists(context: Context, userId: String?, limit: Int = 4): List<Artist> {
        val topArtists = userDao.getTopArtistIds(limit)
        return topArtists.mapNotNull { fav ->
            bandDao.getArtistById(fav.artistId, userId)?.toDomain(context)
        }
    }

    suspend fun getTopBands(context: Context, userId: String?, limit: Int = 4,): List<Band> {
        val topBands = userDao.getTopBandIds(limit)
        return topBands.mapNotNull { fav ->
            bandDao.getBandById(fav.bandId, userId)?.toDomain(context, null, null, null)
        }
    }

    suspend fun getTopGenres(userId: String?, limit: Int = 4): List<Genre> {
        val topGenres = userDao.getTopGenreIds(limit)
        return topGenres.mapNotNull { fav ->
            bandDao.getGenreById(fav.genreId, userId)?.toDomain()
        }
    }



    suspend fun toggleFavorite(
        userId: String,
        itemId: String,
        type: FavoriteType,
        isFavorite: Boolean
    ) {
        if(isFavorite) {
            deleteFavorite(userId, itemId, type)
        } else {
            addFavorite(userId, itemId, type)
        }
    }

    private suspend fun addFavorite(
        userId: String,
        itemId: String,
        type: FavoriteType
    ) {
        when (type) {
            FavoriteType.BAND -> userDao.doUserFavoriteBand(
                UserBandFavoriteEntity(userId, itemId)
            )

            FavoriteType.ARTIST -> userDao.doUserFavoriteArtist(
                UserArtistFavoriteEntity(userId, itemId)
            )

            FavoriteType.ALBUM -> userDao.doUserFavoriteAlbum(
                UserAlbumFavoriteEntity(userId, itemId)
            )

            FavoriteType.GENRE -> userDao.doUserFavoriteGenre(
                UserGenreFavoriteEntity(userId, itemId)
            )

            FavoriteType.SONG -> userDao.doUserFavoriteSong(
                UserSongFavoriteEntity(userId, itemId)
            )
        }
    }


    private suspend fun deleteFavorite(
        userId: String,
        itemId: String,
        type: FavoriteType
    ) {
        when (type) {
            FavoriteType.BAND -> userDao.deleteUserFavoriteBand(userId, itemId)

            FavoriteType.ARTIST -> userDao.deleteUserFavoriteArtist(userId, itemId)

            FavoriteType.ALBUM -> userDao.deleteUserFavoriteAlbum(userId, itemId)

            FavoriteType.GENRE -> userDao.deleteUserFavoriteGenre(userId, itemId)

            FavoriteType.SONG -> userDao.deleteUserFavoriteSong(userId, itemId)
        }
    }



}




object SessionFunctions {
    private const val SP_NAME = "band_world_session"
    private const val USER_ID = "userId"

    fun saveUserId(context: Context, userId: String) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(USER_ID, userId).apply()
    }

    fun getUserId(context: Context): String? {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(USER_ID, null)
    }

    fun clear(context: Context) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().remove(USER_ID).apply()
    }
}



