package cl.duoc.band_world.data

import cl.duoc.band_world.data.seed.InitialData

object DatabaseInitializer {
    suspend fun initializeDatabase(database: BandWorldDatabase) {
        if(database.bandDao().getBandCount() > 0) {
            return
        }

        database.bandDao().insertStatusesInit(InitialData.statuses)
        database.bandDao().insertCountriesInit(InitialData.countries)
        database.bandDao().insertGenresInit(InitialData.genres)
        database.bandDao().insertArtistsInit(InitialData.artists)
        database.bandDao().insertRolesInit(InitialData.roles)
        database.bandDao().insertBandsInit(InitialData.bands)
        database.bandDao().insertBandGenresInit(InitialData.band_genres)
        database.bandDao().insertAlbumsInit(InitialData.albums)
        database.bandDao().insertSongsInit(InitialData.songs)
        database.bandDao().insertMembersInit(InitialData.members)
        database.bandDao().insertMemberRolesInit(InitialData.member_roles)


        database.userDao().insertUsersInit(InitialData.users)
        database.userDao().insertUserAlbumFavoritesInit(InitialData.user_album_favs)
        database.userDao().insertUserArtistFavoritesInit(InitialData.user_artist_favs)
        database.userDao().insertUsertBandFavoritesInit(InitialData.user_band_favs)
        database.userDao().insertUserGenreFavoritesInit(InitialData.user_genre_favs)
        database.userDao().insertUserSongFavorites(InitialData.user_song_favs)
    }
}


