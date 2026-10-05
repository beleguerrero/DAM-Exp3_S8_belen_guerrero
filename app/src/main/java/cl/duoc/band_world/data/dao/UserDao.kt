package cl.duoc.band_world.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import cl.duoc.band_world.data.User
import cl.duoc.band_world.data.entities.AlbumWithRelations
import cl.duoc.band_world.data.entities.ArtistWithRelations
import cl.duoc.band_world.data.entities.BandEntity
import cl.duoc.band_world.data.entities.BandWithRelations
import cl.duoc.band_world.data.entities.GenreEntity
import cl.duoc.band_world.data.entities.GenreWithFavorite
import cl.duoc.band_world.data.entities.SongWithRelations
import cl.duoc.band_world.data.entities.UserAlbumFavoriteEntity
import cl.duoc.band_world.data.entities.UserArtistFavoriteEntity
import cl.duoc.band_world.data.entities.UserBandFavoriteEntity
import cl.duoc.band_world.data.entities.UserEntity
import cl.duoc.band_world.data.entities.UserGenreFavoriteEntity
import cl.duoc.band_world.data.entities.UserSongFavoriteEntity


data class BandFavoriteCount(
    val bandId: String,
    val favoriteCount: Int
)

data class ArtistFavoriteCount(
    val artistId: String,
    val favoriteCount: Int
)

data class GenreFavoriteCount(
    val genreId: String,
    val favoriteCount: Int
)


@Dao
interface UserDao {

    // init
    @Insert
    suspend fun insertUsersInit(users: List<UserEntity>)

    @Insert
    suspend fun insertUserAlbumFavoritesInit(user_album_favs: List<UserAlbumFavoriteEntity>)

    @Insert
    suspend fun insertUserArtistFavoritesInit(user_artist_favs: List<UserArtistFavoriteEntity>)

    @Insert
    suspend fun insertUsertBandFavoritesInit(user_band_favs: List<UserBandFavoriteEntity>)

    @Insert
    suspend fun insertUserGenreFavoritesInit(user_genre_favs: List<UserGenreFavoriteEntity>)

    @Insert
    suspend fun insertUserSongFavorites(user_song_favs: List<UserSongFavoriteEntity>)


    @Query("SELECT COUNT(*) as userCount FROM user")
    suspend fun getUserCount(): Int

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<UserEntity>


    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Transaction
    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Transaction
    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Transaction
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    suspend fun getUserByUsernamePass(username: String, password: String): UserEntity?


    @Query("""
        UPDATE user SET
             name = COALESCE(:name, name),
             username = COALESCE(:username, username),
             email = COALESCE(:email, email),
             password = COALESCE(:password, password)
        WHERE id = :userId
    """)
    suspend fun setUser(
        userId: String,
        name: String?,
        username: String?,
        email: String?,
        password: String?
    ): Int

    @Insert
    suspend fun doUser(user: UserEntity): Long


    // =====================================
    // FAV - BAND
    @Transaction
    @Query("SELECT * FROM user_band_favorites")
    suspend fun getAllUserBandFavs(): List<UserBandFavoriteEntity>

    @Transaction
    @Query("""
        SELECT b.*, 1 AS isFavorite
        FROM band b
        JOIN user_band_favorites ubf on b.id = ubf.bandId
        WHERE ubf.userId = :userId
        LIMIT :limit
    """)
    suspend fun getUserFavoriteBands(userId: String, limit: Int): List<BandWithRelations>


    @Transaction
    @Query("""
        SELECT b.*, 1 AS isFavorite
        FROM band b
        JOIN user_band_favorites ubf on b.id = ubf.bandId
        WHERE ubf.userId = :userId
    """)
    suspend fun getUserFavoriteBands(userId: String): List<BandWithRelations>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun doUserFavoriteBand(ubf: UserBandFavoriteEntity): Long


    @Query("DELETE FROM user_band_favorites WHERE userId = :userId AND bandId = :bandId")
    suspend fun deleteUserFavoriteBand(userId: String, bandId: String): Int



    // =====================================
    // FAV - ARTIST
    @Transaction
    @Query("SELECT * FROM user_artist_favorites")
    suspend fun getAllUserArtistFavs(): List<UserArtistFavoriteEntity>


    @Transaction
    @Query("""
        SELECT a.*, 1 AS isFavorite
        from artist a
        JOIN user_artist_favorites uaf on a.id = uaf.artistId
        WHERE uaf.userId = :userId
        LIMIT :limit
    """)
    suspend fun getUserFavoriteArtists(userId: String, limit: Int): List<ArtistWithRelations>


    @Transaction
    @Query("""
        SELECT a.*, 1 as isFavorite
        from artist a
        JOIN user_artist_favorites uaf on a.id = uaf.artistId
        WHERE uaf.userId = :userId
    """)
    suspend fun getUserFavoriteArtists(userId: String): List<ArtistWithRelations>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun doUserFavoriteArtist(uaf: UserArtistFavoriteEntity): Long


    @Query("DELETE FROM user_artist_favorites WHERE userId = :userId AND artistId = :artistId")
    suspend fun deleteUserFavoriteArtist(userId: String, artistId: String): Int



    // =====================================
    // FAV - ALBUM
    @Transaction
    @Query("SELECT * FROM user_album_favorites")
    suspend fun getAllUserAlbumFavs(): List<UserAlbumFavoriteEntity>


    @Transaction
    @Query("""
        SELECT a.*, 1 AS isFavorite
        FROM album a
        JOIN user_album_favorites uaf on a.id = uaf.albumId
        WHERE uaf.userId = :userId
        LIMIT :limit
    """)
    suspend fun getUserFavoriteAlbums(userId: String, limit: Int): List<AlbumWithRelations>


    @Transaction
    @Query("""
        SELECT a.*, 1 AS isFavorite
        from album a
        JOIN user_album_favorites uaf on a.id = uaf.albumId
        WHERE uaf.userId = :userId
    """)
    suspend fun getUserFavoriteAlbums(userId: String): List<AlbumWithRelations>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun doUserFavoriteAlbum(uaf: UserAlbumFavoriteEntity): Long


    @Query("DELETE FROM user_album_favorites WHERE userId = :userId AND albumId = :albumId")
    suspend fun deleteUserFavoriteAlbum(userId: String, albumId: String): Int



    // =====================================
    // FAV - GENRE
    @Transaction
    @Query("SELECT * FROM user_genre_favorites")
    suspend fun getAllUserGenreFavs(): List<UserGenreFavoriteEntity>



    @Transaction
    @Query("""
        SELECT g.*, 1 AS isFavorite
        from genre g
        JOIN user_genre_favorites ugf on g.id = ugf.genreId
        WHERE ugf.userId = :userId
        LIMIT :limit
    """)
    suspend fun getUserFavoriteGenres(userId: String, limit: Int): List<GenreWithFavorite>


    @Transaction
    @Query("""
        SELECT g.*, 1 AS isFavorite
        from genre g
        JOIN user_genre_favorites ugf on g.id = ugf.genreId
        WHERE ugf.userId = :userId
    """)
    suspend fun getUserFavoriteGenres(userId: String): List<GenreWithFavorite>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun doUserFavoriteGenre(ugf: UserGenreFavoriteEntity): Long


    @Query("DELETE FROM user_genre_favorites WHERE userId = :userId AND genreId = :genreId")
    suspend fun deleteUserFavoriteGenre(userId: String, genreId: String): Int



    // =====================================
    // FAV - SONG
    @Transaction
    @Query("SELECT * FROM user_song_favorites")
    suspend fun getAllUserSongFavs(): List<UserSongFavoriteEntity>


    @Transaction
    @Query("""
        SELECT s.*, 1 AS isFavorite
        from song s
        JOIN user_song_favorites usf on s.id = usf.songId
        WHERE usf.userId = :userId
        LIMIT :limit
    """)
    suspend fun getUserFavoriteSongs(userId: String, limit: Int): List<SongWithRelations>


    @Transaction
    @Query("""
        SELECT s.*, 1 AS isFavorite
        from song s
        JOIN user_song_favorites usf on s.id = usf.songId
        WHERE usf.userId = :userId
    """)
    suspend fun getUserFavoriteSongs(userId: String): List<SongWithRelations>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun doUserFavoriteSong(usf: UserSongFavoriteEntity): Long


    @Query("DELETE FROM user_song_favorites WHERE userId = :userId AND songId = :songId")
    suspend fun deleteUserFavoriteSong(userId: String, songId: String): Int



    // =====================================
    // TOP!!!
    @Transaction
    @Query("""
        SELECT bandId, COUNT(*) as favoriteCount
        FROM user_band_favorites
        GROUP BY bandId
        ORDER BY favoriteCount DESC
        LIMIT :limit
    """)
    suspend fun getTopBandIds(limit: Int): List<BandFavoriteCount>


    @Transaction
    @Query("""
        SELECT artistId, COUNT(*) as favoriteCount
        FROM user_artist_favorites
        GROUP BY artistId
        ORDER BY favoriteCount DESC
        LIMIT :limit
    """)
    suspend fun getTopArtistIds(limit: Int): List<ArtistFavoriteCount>


    @Transaction
    @Query("""
        SELECT genreId, COUNT(*) as favoriteCount
        FROM user_genre_favorites
        GROUP BY genreId
        ORDER BY favoriteCount DESC
        LIMIT :limit
    """)
    suspend fun getTopGenreIds(limit: Int): List<GenreFavoriteCount>


}
