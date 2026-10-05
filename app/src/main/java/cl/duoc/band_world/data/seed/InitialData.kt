package cl.duoc.band_world.data.seed

import cl.duoc.band_world.data.entities.AlbumEntity
import cl.duoc.band_world.data.entities.ArtistEntity
import cl.duoc.band_world.data.entities.BandEntity
import cl.duoc.band_world.data.entities.BandGenreCrossRef
import cl.duoc.band_world.data.entities.CountryEntity
import cl.duoc.band_world.data.entities.GenreEntity
import cl.duoc.band_world.data.entities.MemberEntity
import cl.duoc.band_world.data.entities.MemberRoleCrossRef
import cl.duoc.band_world.data.entities.RoleEntity
import cl.duoc.band_world.data.entities.SongEntity
import cl.duoc.band_world.data.entities.StatusEntity
import cl.duoc.band_world.data.entities.UserAlbumFavoriteEntity
import cl.duoc.band_world.data.entities.UserArtistFavoriteEntity
import cl.duoc.band_world.data.entities.UserBandFavoriteEntity
import cl.duoc.band_world.data.entities.UserEntity
import cl.duoc.band_world.data.entities.UserGenreFavoriteEntity
import cl.duoc.band_world.data.entities.UserSongFavoriteEntity
import java.util.UUID

object InitialData {

    // ** STATUS **
    val status_active = StatusEntity(id = UUID.randomUUID().toString(), name = "Activo")
    val status_split = StatusEntity(id = UUID.randomUUID().toString(), name = "Desintegrada")
    val statuses = listOf(status_active, status_split)


    // ** COUNTRIES **
    val country_usa = CountryEntity(
        id = UUID.randomUUID().toString(),
        name = "Estados Unidos",
        emoji = "\uD83C\uDDFA\uD83C\uDDF8"
    )
    val country_sweden = CountryEntity(
        id = UUID.randomUUID().toString(),
        name = "Suecia",
        emoji = "\uD83C\uDDF8\uD83C\uDDEA"
    )
    val countries = listOf(country_usa, country_sweden)


    // ** GENRES **
    val genre_grunge = GenreEntity(id = UUID.randomUUID().toString(), name = "Grunge")
    val genre_numetal = GenreEntity(id = UUID.randomUUID().toString(), name = "Nu Metal")
    val genre_alt = GenreEntity(id = UUID.randomUUID().toString(), name = "Alternativo")
    val genre_melodic = GenreEntity(id = UUID.randomUUID().toString(), name = "Death Metal Melódico")
    val genres = listOf(genre_grunge, genre_numetal, genre_alt, genre_melodic)



    // ** ARTISTS **
    val artist_layne_staley = ArtistEntity(
        id = UUID.randomUUID().toString(),
        name = "Layne Staley",
        description = "",
        birthDate = "1967-08-22",
        deathDate = "2002-04-05",
        countryId = country_usa.id,
    )
    val artist_jerry_cantrell = ArtistEntity(
        id = UUID.randomUUID().toString(),
        name = "Jerry Cantrell",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum hendrerit ligula sit amet nulla lobortis, a dignissim nulla porttitor. Proin ac urna pulvinar elit mollis tristique. Donec in risus euismod, pellentesque tortor in, sagittis ante. Nunc et ex velit. Pellentesque rhoncus, dolor nec dictum placerat, odio lorem hendrerit ligula, ut imperdiet metus orci ut sem.",
        birthDate = "1966-03-18",
        countryId = country_usa.id
    )
    val artist_chino_moreno = ArtistEntity(
        id = UUID.randomUUID().toString(),
        name = "Chino Moreno",
        description = "",
        birthDate = "1973-06-20",
        countryId = country_usa.id
    )
    val artist_stephen_carpenter = ArtistEntity(
        id = UUID.randomUUID().toString(),
        name = "Stephen Carpenter",
        description = "",
        birthDate = "1970-08-03",
        countryId = country_usa.id
    )
    val artist_anders_friden = ArtistEntity(
        id = UUID.randomUUID().toString(),
        name = "Anders Fridén",
        description = "",
        birthDate = "1973-03-25",
        countryId = country_sweden.id
    )
    val artist_bjorn_gelotte = ArtistEntity(
        id = UUID.randomUUID().toString(),
        name = "Björn Gelotte",
        description = "",
        birthDate = "1975-08-27",
        countryId = country_sweden.id
    )
    val artists = listOf(
        artist_layne_staley,
        artist_jerry_cantrell,
        artist_chino_moreno,
        artist_stephen_carpenter,
        artist_anders_friden,
        artist_bjorn_gelotte
    )


    // ** ROLES **
    val role_vocals     = RoleEntity(id = UUID.randomUUID().toString(), name = "Cantante", image = "vocal")
    val role_guitars    = RoleEntity(id = UUID.randomUUID().toString(), name = "Guitarra", image = "guitar")
    val role_drums      = RoleEntity(id = UUID.randomUUID().toString(), name = "Batería", image = "drum")
    val role_bass       = RoleEntity(id = UUID.randomUUID().toString(), name = "Bajo", image = "bass")
    val roles = listOf(role_vocals, role_guitars, role_drums, role_bass)



    // ** BANDS **
    val band_aic = BandEntity(
        id = UUID.randomUUID().toString(),
        name = "Alice in Chains",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum hendrerit ligula sit amet nulla lobortis, a dignissim nulla porttitor. Proin ac urna pulvinar elit mollis tristique. Donec in risus euismod, pellentesque tortor in, sagittis ante. Nunc et ex velit. Pellentesque rhoncus, dolor nec dictum placerat, odio lorem hendrerit ligula, ut imperdiet metus orci ut sem.",
        image = "band_alice_in_chains",
        statusId = status_active.id,
        countryId = country_usa.id,
        yearFounded = 1985
    )

    val band_deftones = BandEntity(
        id = UUID.randomUUID().toString(),
        name = "Deftones",
        description = "",
        image = "band_deftones",
        statusId = status_active.id,
        countryId = country_usa.id,
        yearFounded = 1988,
    )

    val band_if = BandEntity(
        id = UUID.randomUUID().toString(),
        name = "In Flames",
        description = "",
        image = "band_in_flames",
        statusId = status_active.id,
        countryId = country_sweden.id,
        yearFounded = 1990,
    )
    val bands = listOf(band_aic, band_deftones, band_if)


    // ** BAND - GENRE **
    val band_genres = listOf(
        BandGenreCrossRef(band_aic.id, genre_grunge.id),
        BandGenreCrossRef(band_deftones.id, genre_numetal.id),
        BandGenreCrossRef(band_deftones.id, genre_alt.id),
        BandGenreCrossRef(band_if.id, genre_melodic.id),
    )


    // ** ALBUMS **
    val album_facelift = AlbumEntity(
        id = UUID.randomUUID().toString(),
        bandId = band_aic.id,
        name = "Facelift",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum hendrerit ligula sit amet nulla lobortis, a dignissim nulla porttitor. Proin ac urna pulvinar elit mollis tristique. Donec in risus euismod, pellentesque tortor in, sagittis ante. Nunc et ex velit. Pellentesque rhoncus, dolor nec dictum placerat, odio lorem hendrerit ligula, ut imperdiet metus orci ut sem.",
        releaseDate = "1990-08-28",
    )
    val album_dirt = AlbumEntity(
        id = UUID.randomUUID().toString(),
        bandId = band_aic.id,
        name = "Dirt",
        description = "",
        releaseDate = "1992-09-29",
    )
    val albums = listOf(album_facelift, album_dirt)



    // ** SONGS **
    val song_facelift_we_die_young      = SongEntity(id = UUID.randomUUID().toString(), albumId = album_facelift.id, number = 1, name = "We Die Young")
    val song_facelift_man_in_the_box    = SongEntity(id = UUID.randomUUID().toString(), albumId = album_facelift.id, number = 2, name = "Man in the Box")
    val song_facelift_sea_of_sorrow     = SongEntity(id = UUID.randomUUID().toString(), albumId = album_facelift.id, number = 3, name = "Sea of Sorrow")
    val song_dirt_them_bones            = SongEntity(id = UUID.randomUUID().toString(), albumId = album_dirt.id, number = 1, name = "Them Bones")
    val song_dirt_dam_that_river        = SongEntity(id = UUID.randomUUID().toString(), albumId = album_dirt.id, number = 2, name = "Dam That River")
    val song_dirt_rain_when_i_die       = SongEntity(id = UUID.randomUUID().toString(), albumId = album_dirt.id, number = 3, name = "Rain When I Die")
    val songs = listOf(
        song_facelift_we_die_young, song_facelift_man_in_the_box, song_facelift_sea_of_sorrow, song_dirt_them_bones, song_dirt_dam_that_river, song_dirt_rain_when_i_die
    )




    // ** MEMBERS **
    val member_aic_layne_staley = MemberEntity(
        id = UUID.randomUUID().toString(),
        bandId = band_aic.id,
        artistId = artist_layne_staley.id,
        startYear = 1987,
        endYear = 2002,
    )


    val member_aic_jerry_cantrell = MemberEntity(
        id = UUID.randomUUID().toString(),
        bandId = band_aic.id,
        artistId = artist_jerry_cantrell.id,
        startYear = 1987,
    )

    val member_def_chino_moreno = MemberEntity(
        id = UUID.randomUUID().toString(),
        bandId = band_deftones.id,
        artistId = artist_chino_moreno.id,
        startYear = 1988,
    )

    val member_def_stephen_carpenter = MemberEntity(
        id = UUID.randomUUID().toString(),
        bandId = band_deftones.id,
        artistId = artist_stephen_carpenter.id,
        startYear = 1988,
    )

    val member_if_anders_friden = MemberEntity(
        id = UUID.randomUUID().toString(),
        bandId = band_if.id,
        artistId = artist_anders_friden.id,
        startYear = 1995,
    )

    val member_in_bjorn_gelotte = MemberEntity(
        id = UUID.randomUUID().toString(),
        bandId = band_if.id,
        artistId = artist_bjorn_gelotte.id,
        startYear = 1995,
    )
    val members = listOf(
        member_aic_layne_staley,member_aic_jerry_cantrell,
        member_def_chino_moreno, member_def_stephen_carpenter,
        member_if_anders_friden, member_in_bjorn_gelotte
    )


    // ** MEMBER - ROLE **
    val member_roles = listOf(
        MemberRoleCrossRef(memberId = member_aic_layne_staley.id, roleId = role_vocals.id),
        MemberRoleCrossRef(memberId = member_aic_jerry_cantrell.id, roleId = role_guitars.id),
        MemberRoleCrossRef(memberId = member_def_chino_moreno.id, roleId = role_vocals.id),
        MemberRoleCrossRef(memberId = member_def_stephen_carpenter.id, roleId = role_guitars.id),
        MemberRoleCrossRef(memberId = member_if_anders_friden.id, roleId = role_vocals.id),
        MemberRoleCrossRef(memberId = member_in_bjorn_gelotte.id, roleId = role_guitars.id),
    )






    // ========================================
    // ========================================
    // ========================================
    // ========================================
    // ========================================

    val user_yo     = UserEntity(id = UUID.randomUUID().toString(), name = "Belén Guerrero", username = "bele_g", email = "belen@correo.com", password = "pass1")
    val user_edward = UserEntity(id = UUID.randomUUID().toString(), name = "Edward Cullen", username = "edward_c", email = "edward@correo.com", password = "pass1")
    val user_bella  = UserEntity(id = UUID.randomUUID().toString(), name = "Bella Swan", username = "bella_s", email = "bella@correo.com", password = "pass1")
    val user_regina = UserEntity(id = UUID.randomUUID().toString(), name = "Regina George", username = "regi_g", email = "regina@correo.com", password = "pass1")
    val user_cady   = UserEntity(id = UUID.randomUUID().toString(), name = "Cady Heron", username = "cady_h", email = "cady@correo.com", password = "pass1")
    val users = listOf(
        user_yo,
        user_edward,
        user_bella,
        user_regina,
        user_cady
    )




    // ** FAVORITES **
    val user_band_favs = listOf(
        UserBandFavoriteEntity(user_yo.id, band_aic.id),
        UserBandFavoriteEntity(user_yo.id, band_deftones.id),
        UserBandFavoriteEntity(user_yo.id, band_if.id),

        UserBandFavoriteEntity(user_edward.id, band_aic.id),
        UserBandFavoriteEntity(user_edward.id, band_deftones.id),

        UserBandFavoriteEntity(user_bella.id, band_aic.id),
        UserBandFavoriteEntity(user_bella.id, band_deftones.id),

        UserBandFavoriteEntity(user_regina.id, band_aic.id),

        UserBandFavoriteEntity(user_cady.id, band_aic.id),
    )

    val user_genre_favs = listOf(
        UserGenreFavoriteEntity(user_yo.id, genre_grunge.id),
        UserGenreFavoriteEntity(user_yo.id, genre_numetal.id),
        UserGenreFavoriteEntity(user_yo.id, genre_melodic.id),

        UserGenreFavoriteEntity(user_regina.id, genre_grunge.id),
        UserGenreFavoriteEntity(user_cady.id, genre_grunge.id),
    )

    val user_artist_favs = listOf(
        UserArtistFavoriteEntity(user_yo.id, artist_layne_staley.id),
        UserArtistFavoriteEntity(user_yo.id, artist_jerry_cantrell.id),
        UserArtistFavoriteEntity(user_yo.id, artist_chino_moreno.id),
        UserArtistFavoriteEntity(user_yo.id, artist_anders_friden.id),
        UserArtistFavoriteEntity(user_yo.id, artist_bjorn_gelotte.id),

        UserArtistFavoriteEntity(user_edward.id, artist_chino_moreno.id),
        UserArtistFavoriteEntity(user_edward.id, artist_layne_staley.id),

        UserArtistFavoriteEntity(user_bella.id, artist_chino_moreno.id),
        UserArtistFavoriteEntity(user_bella.id, artist_layne_staley.id),
        UserArtistFavoriteEntity(user_bella.id, artist_jerry_cantrell.id),

        UserArtistFavoriteEntity(user_regina.id, artist_chino_moreno.id),
        UserArtistFavoriteEntity(user_regina.id, artist_anders_friden.id),

        UserArtistFavoriteEntity(user_cady.id, artist_chino_moreno.id),
        UserArtistFavoriteEntity(user_cady.id, artist_anders_friden.id),
    )

    val user_album_favs = listOf(
        UserAlbumFavoriteEntity(user_yo.id, album_facelift.id),
        UserAlbumFavoriteEntity(user_yo.id, album_dirt.id),
    )

    val user_song_favs = listOf(
        UserSongFavoriteEntity(user_yo.id, song_dirt_them_bones.id),
        UserSongFavoriteEntity(user_yo.id, song_dirt_dam_that_river.id),
        UserSongFavoriteEntity(user_yo.id, song_dirt_rain_when_i_die.id),
        UserSongFavoriteEntity(user_yo.id, song_facelift_we_die_young.id),
        UserSongFavoriteEntity(user_yo.id, song_facelift_sea_of_sorrow.id),
        UserSongFavoriteEntity(user_yo.id, song_facelift_man_in_the_box.id),
    )

}