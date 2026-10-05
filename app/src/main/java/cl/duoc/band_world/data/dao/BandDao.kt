package cl.duoc.band_world.data.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import cl.duoc.band_world.data.Names
import cl.duoc.band_world.data.entities.AlbumEntity
import cl.duoc.band_world.data.entities.AlbumWithFavorite
import cl.duoc.band_world.data.entities.AlbumWithRelations
import cl.duoc.band_world.data.entities.ArtistEntity
import cl.duoc.band_world.data.entities.ArtistWithRelations
import cl.duoc.band_world.data.entities.BandEntity
import cl.duoc.band_world.data.entities.BandGenreCrossRef
import cl.duoc.band_world.data.entities.BandWithRelations
import cl.duoc.band_world.data.entities.CountryEntity
import cl.duoc.band_world.data.entities.GenreEntity
import cl.duoc.band_world.data.entities.GenreWithFavorite
import cl.duoc.band_world.data.entities.MemberEntity
import cl.duoc.band_world.data.entities.MemberRoleCrossRef
import cl.duoc.band_world.data.entities.MemberWithArtistsFavorite
import cl.duoc.band_world.data.entities.MemberWithRelations
import cl.duoc.band_world.data.entities.RoleEntity
import cl.duoc.band_world.data.entities.SongEntity
import cl.duoc.band_world.data.entities.SongWithFavorite
import cl.duoc.band_world.data.entities.SongWithRelations
import cl.duoc.band_world.data.entities.StatusEntity


data class GenreBandCount(
    val genreId: String,
    val bandCount: Int
)

@Dao
interface BandDao {
    // init
    @Insert
    suspend fun insertBandsInit(bands: List<BandEntity>)

    @Insert
    suspend fun insertBandGenresInit(bandGenres: List<BandGenreCrossRef>)

    @Insert
    suspend fun insertAlbumsInit(albums: List<AlbumEntity>)

    @Insert
    suspend fun insertArtistsInit(artists: List<ArtistEntity>)

    @Insert
    suspend fun insertCountriesInit(countries: List<CountryEntity>)

    @Insert
    suspend fun insertGenresInit(genres: List<GenreEntity>)

    @Insert
    suspend fun insertMembersInit(members: List<MemberEntity>)

    @Insert
    suspend fun insertMemberRolesInit(memberRoles: List<MemberRoleCrossRef>)

    @Insert
    suspend fun insertRolesInit(roles: List<RoleEntity>)

    @Insert
    suspend fun insertSongsInit(songs: List<SongEntity>)

    @Insert
    suspend fun insertStatusesInit(statuses: List<StatusEntity>)


    @Query("SELECT COUNT(*) as bandCount FROM band")
    suspend fun getBandCount(): Int


    @Transaction
    @Query("""
        SELECT
            b.*,
            CASE
                WHEN :userId IS NOT NULL AND ubf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM band b
        LEFT JOIN user_band_favorites ubf ON b.id = ubf.bandId AND ubf.userId = :userId
    """)
    suspend fun getAllBands(userId: String?): List<BandWithRelations>



    @Transaction
    @Query("""
        SELECT 
            b.*,
            CASE
                WHEN :userId IS NOT NULL AND ubf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM band b
        LEFT JOIN user_band_favorites ubf ON b.id = ubf.bandId AND ubf.userId = :userId
        WHERE b.id = :id
    """)
    suspend fun getBandById(id: String, userId: String?): BandWithRelations?



    @Transaction
    @Query("""
        SELECT 
            b.*,
            CASE
                WHEN :userId IS NOT NULL AND ubf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM band b
        JOIN member m on b.id = m.bandId
        JOIN artist a on a.id = m.artistId
        LEFT JOIN user_band_favorites ubf ON b.id = ubf.bandId AND ubf.userId = :userId
        WHERE a.id = :artistId
    """)
    suspend fun getBandsByArtist(artistId: String, userId: String?): List<BandWithRelations>



    @Transaction
    @Query("""
        SELECT
        m.*,
        CASE
            WHEN :userId IS NOT NULL AND uaf.userId IS NOT NULL THEN 1
            ELSE 0
        END AS isFavorite
        FROM member m
        LEFT JOIN user_artist_favorites uaf ON m.artistId = uaf.artistId AND uaf.userId = :userId
        WHERE m.bandId = :bandId
    """)
    suspend fun getMembersByBand(bandId: String, userId: String?): List<MemberWithArtistsFavorite>


//    @Transaction
//    @Query("""
//        SELECT
//        a.*,
//        CASE
//            WHEN :userId IS NOT NULL AND uaf.userId IS NOT NULL THEN 1
//            ELSE 0
//        END AS isFavorite
//        FROM member m
//        JOIN artist a
//        LEFT JOIN user_artist_favorites uaf ON a.id = uaf.artistId AND uaf.userId = :userId
//        WHERE m.id = :memberId
//    """)
//    suspend fun getArtistsByMember(memberId: String, userId: String?): List<ArtistWithFavorite>


//    @Transaction
//    @Query("""
//        SELECT
//        a.*,
//        CASE
//            WHEN :userId IS NOT NULL AND uaf.userId IS NOT NULL THEN 1
//            ELSE 0
//        END
//        FROM member m
//        JOIN artist a ON m.artistId = a.id
//        LEFT JOIN user_artist_favorites uaf ON a.id = uaf.artistId AND uaf.userId = :userId
//        WHERE m.bandId = :bandId
//    """)
//    suspend fun getArtistsByBand(bandId: String, userId: String?): List<ArtistWithRelations>



    @Query("""
        SELECT 
            g.*,
            CASE
                WHEN :userId IS NOT NULL AND ugf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM genre g
        LEFT JOIN user_genre_favorites ugf ON g.id = ugf.genreId AND ugf.userId = :userId
    """)
    suspend fun getAllGenres(userId: String?): List<GenreWithFavorite>



    @Transaction
    @Query("""
        SELECT g.*,
            CASE
                WHEN :userId IS NOT NULL AND ugf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM genre g
        LEFT JOIN user_genre_favorites ugf ON g.id = ugf.genreId AND ugf.userId = :userId
        WHERE g.id = :id
    """)
    suspend fun getGenreById(id: String, userId: String?): GenreWithFavorite?

    @Query("SELECT genreId, COUNT(*) AS bandCount FROM band_genre GROUP BY genreId")
    suspend fun getCountBandsByGenre(): List<GenreBandCount>



    @Transaction
    @Query("""
        SELECT
            g.*,
            CASE
                WHEN :userId IS NOT NULL AND ugf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM genre g
        JOIN band_genre bg ON g.id = bg.genreId
        LEFT JOIN user_genre_favorites ugf ON g.id = ugf.genreId AND ugf.userId = :userId
        WHERE bg.bandId = :bandId
    """)
    suspend fun getGenreByBand(bandId: String, userId: String?): List<GenreWithFavorite>

    @Transaction
    @Query("""
        SELECT 
            b.*,
            CASE
                WHEN :userId IS NOT NULL AND ubf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM band b
        JOIN band_genre bg on b.id = bg.bandId
        LEFT JOIN user_band_favorites ubf ON b.id = ubf.bandId AND ubf.userId = :userId
        WHERE bg.genreId = :genreId
    """)
    suspend fun getBandsByGenre(genreId: String, userId: String?): List<BandWithRelations>


    @Transaction
    @Query("""
        SELECT 
            a.*,
            CASE
                WHEN :userId IS NOT NULL AND uaf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM album a
        LEFT JOIN user_album_favorites uaf ON a.id = uaf.albumId AND uaf.userId = :userId
    """)
    suspend fun getAllAlbums(userId: String?): List<AlbumWithRelations>



    @Transaction
    @Query("""
        SELECT 
            a.*,
            CASE
                WHEN :userId IS NOT NULL AND uaf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM album a
        LEFT JOIN user_album_favorites uaf ON a.id = uaf.albumId AND uaf.userId = :userId
        WHERE a.id = :id
    """)
    suspend fun getAlbumById(id: String, userId: String?): AlbumWithRelations?



    @Transaction
    @Query("""
        SELECT
            a.*,
            CASE
                WHEN :userId IS NOT NULL AND uaf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM album a
        LEFT JOIN user_album_favorites uaf ON a.id = uaf.albumId AND uaf.userId = :userId
        WHERE a.bandId = :bandId
    """)
    suspend fun getAlbumsByBand(bandId: String, userId: String?): List<AlbumWithFavorite>?


    @Transaction
    @Query("""
        SELECT 
            b.*,
            CASE
                WHEN :userId IS NOT NULL AND ubf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM band b
        JOIN album a ON b.id = a.bandId
        LEFT JOIN user_band_favorites ubf ON b.id = ubf.bandId AND ubf.userId = :userId
        WHERE a.id = :albumId
    """)
    suspend fun getBandByAlbum(albumId: String, userId: String?): BandWithRelations?


    @Query("""
        SELECT 
            s.*,
            CASE
                WHEN :userId IS NOT NULL AND usf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM song s
        LEFT JOIN user_song_favorites usf ON s.id = usf.songId AND usf.userId = :userId
    """)
    suspend fun getAllSongs(userId: String?): List<SongWithRelations>

    @Transaction
    @Query("""
        SELECT 
            s.*,
            CASE
                WHEN :userId IS NOT NULL AND usf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite 
        FROM song s 
        LEFT JOIN user_song_favorites usf ON s.id = usf.songId AND usf.userId = :userId
        WHERE s.id = :id
    """)
    suspend fun getSongById(id: String, userId: String?): SongWithRelations?



    @Transaction
    @Query("""
        SELECT b.name FROM band b
        JOIN album a ON b.id = a.bandId
        JOIN song s ON a.id = s.albumId
        WHERE s.id = :songId
    """)
    suspend fun getBandNameBySong(songId: String): String?


    @Transaction
    @Query("""
        SELECT
        s.*, 
        CASE
            WHEN :userId IS NOT NULL AND usf.userId IS NOT NULL THEN 1
            ELSE 0
        END AS isFavorite
        FROM song s
        LEFT JOIN user_song_favorites usf ON s.id = usf.songId AND usf.userId = :userId
        WHERE s.albumId = :albumId
    """)
    suspend fun getSongsByAlbum(albumId: String, userId: String?): List<SongWithFavorite>



    @Query("""
        SELECT 
        a.*,
            CASE
                WHEN :userId IS NOT NULL AND uaf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM artist a
        LEFT JOIN user_artist_favorites uaf ON a.id = uaf.artistId AND uaf.userId = :userId
    """)
    suspend fun getAllArtists(userId: String?): List<ArtistWithRelations>


    @Transaction
    @Query("""
        SELECT 
            a.*,
            CASE
                WHEN :userId IS NOT NULL AND uaf.userId IS NOT NULL THEN 1
                ELSE 0
            END AS isFavorite
        FROM artist a
        LEFT JOIN user_artist_favorites uaf ON a.id = uaf.artistId AND uaf.userId = :userId
        WHERE a.id = :id
    """)
    suspend fun getArtistById(id: String, userId: String?): ArtistWithRelations?




    @Query("SELECT id, name FROM genre")
    suspend fun getGenreNames(): List<Names>

    @Query("SELECT id, name FROM country")
    suspend fun getCountryNames(): List<Names>

    @Query("SELECT id, name FROM status")
    suspend fun getStatusNames(): List<Names>


}
