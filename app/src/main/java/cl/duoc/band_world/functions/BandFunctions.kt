package cl.duoc.band_world.functions

import android.content.Context
import cl.duoc.band_world.data.Album
import cl.duoc.band_world.data.Artist
import cl.duoc.band_world.data.Band
import cl.duoc.band_world.data.Genre
import cl.duoc.band_world.data.Member
import cl.duoc.band_world.data.Names
import cl.duoc.band_world.data.Song
import cl.duoc.band_world.data.dao.BandDao
import cl.duoc.band_world.data.entities.AlbumWithFavorite
import cl.duoc.band_world.data.entities.MemberEntity
import cl.duoc.band_world.data.entities.SongWithFavorite
import cl.duoc.band_world.data.toDomain
import cl.duoc.band_world.ui.components.Filters
import cl.duoc.band_world.ui.components.ListaFiltro


data class AlbumWithSongs(
    val album: AlbumWithFavorite,
    val songs: List<SongWithFavorite>
)

class BandFunctions(
    private val bandDao: BandDao
) {

    fun getBandsFilter(
        bands: List<Band>?,
        includes: List<ListaFiltro>,
        excludes: List<ListaFiltro>
    ): List<Band>? {

        return bands?.filter { band ->

            // include
            val matchInclude = includes.all { listaFiltro ->
                listaFiltro.selected.any { id ->
                    when(listaFiltro.filter) {
                        Filters.GENRE -> band.genres.any { it.id == id }
                        Filters.COUNTRY -> band.country.id == id
                        Filters.STATUS -> band.status.id == id
                    }
                }
            }

            // exclude
            val matchExclude = excludes.all { listaFiltro ->
                listaFiltro.selected.none { id ->
                    when(listaFiltro.filter) {
                        Filters.GENRE -> band.genres.any { it.id == id }
                        Filters.COUNTRY -> band.country.id == id
                        Filters.STATUS -> band.status.id == id
                    }
                }
            }

            matchInclude && matchExclude
        }
    }

    suspend fun getAllBands(
        context: Context,
        userId: String?,
    ): List<Band> {
        val bands = bandDao.getAllBands(userId)

        return bands.map {
            it.toDomain(context, null, null, null)
        }
    }


    suspend fun getBandById(context: Context, bandId: String, userId: String?): Band? {
        try {

            val band = bandDao.getBandById(bandId, userId) ?: return null

            val genres = bandDao.getGenreByBand(bandId, userId)

            val members = bandDao.getMembersByBand(bandId, userId)

            val albums = bandDao.getAlbumsByBand(bandId, userId)

            val albumsWithSongs = albums?.map { album ->
                val songs = bandDao.getSongsByAlbum(album.album.id, userId)
                AlbumWithSongs(
                    album =  album,
                    songs = songs
                )
            }


            val r = band.toDomain(
                context = context,
                genres = genres,
                members = members,
                albums = albumsWithSongs
            )
            return r
        } catch (e: Exception) {
            println("*** ERROR: ${e.message}")
            return null
        }
    }

//    suspend fun getArtistsByBand(context: Context, bandId: String, userId: String?): List<Artist> {
//        return bandDao.getArtistsByBand(bandId, userId).map { it.toDomain(context) }
//    }

    suspend fun getBandsByArtist(context: Context, artistId: String, userId: String?): List<Band> {
        val bands = bandDao.getBandsByArtist(artistId, userId)
        return bands.map {
            it.toDomain(context, null, null, null)
        }
    }

//    suspend fun getMembersByBand(context: Context, bandId: String, userId: String?): List<Member> {
//        val members = bandDao.getMembersByBand(bandId, userId)
//        return members.map {
//            it.toDomain(context)
//        }
//    }


    // =============


    suspend fun getAllGenres(userId: String?): List<Genre> {
        val genres = bandDao.getAllGenres(userId)
        return genres.map {
            it.toDomain()
        }
    }

    suspend fun getGenreById(context: Context, genreId: String, userId: String?): Genre? {
        val genre = bandDao.getGenreById(genreId, userId)
        return genre?.toDomain()
    }


    suspend fun getBandsByGenre(context: Context, genreId: String, userId: String?): List<Band> {
        val bands = bandDao.getBandsByGenre(genreId, userId)
        return bands.map {
            it.toDomain(context, null, null, null)
        }
    }

    suspend fun getCountBandsByGenre(): Map<String, Int> {
        return bandDao.getCountBandsByGenre().associate {
            it.genreId to it.bandCount
        }
    }


//    suspend fun getAllAlbums(context: Context): List<Album> {
//        val albums = bandDao.getAllAlbums()
//        return albums.map {
//            it.toDomain(context)
//        }
//    }


    suspend fun getAlbumById(context: Context, albumId: String, userId: String?): Album? {
        val album = bandDao.getAlbumById(albumId, userId)
        val songs = bandDao.getSongsByAlbum(albumId, userId)
        return album?.toDomain(context, songs)
    }

    suspend fun getBandByAlbum(context: Context, albumId: String, userId: String?): Band? {
        val band = bandDao.getBandByAlbum(albumId, userId)
        return band?.toDomain(context, null, null, null)
    }


    suspend fun getAllArtists(context: Context, userId: String?): List<Artist> {
        val artists = bandDao.getAllArtists(userId)
        return artists.map {
            it.toDomain(context)
        }
    }

    suspend fun getArtistById(context: Context, artistId: String, userId: String?): Artist? {
        val artist = bandDao.getArtistById(artistId, userId)
        return artist?.toDomain(context)
    }


    suspend fun getGenreNames(): List<Names> {
        return bandDao.getGenreNames()
    }

    suspend fun getCountryNames(): List<Names> {
        return bandDao.getCountryNames()
    }

    suspend fun getStatusNames(): List<Names> {
        return bandDao.getStatusNames()
    }


}


