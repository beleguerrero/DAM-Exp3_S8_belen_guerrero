package cl.duoc.band_world.data

import android.content.Context
import cl.duoc.band_world.data.entities.AlbumEntity
import cl.duoc.band_world.data.entities.AlbumWithFavorite
import cl.duoc.band_world.data.entities.AlbumWithRelations
import cl.duoc.band_world.data.entities.ArtistEntity
import cl.duoc.band_world.data.entities.ArtistWithFavorite
import cl.duoc.band_world.data.entities.ArtistWithRelations
import cl.duoc.band_world.data.entities.BandWithRelations
import cl.duoc.band_world.data.entities.CountryEntity
import cl.duoc.band_world.data.entities.GenreEntity
import cl.duoc.band_world.data.entities.GenreWithFavorite
import cl.duoc.band_world.data.entities.MemberEntity
import cl.duoc.band_world.data.entities.MemberWithArtistsFavorite
import cl.duoc.band_world.data.entities.RoleEntity
import cl.duoc.band_world.data.entities.SongEntity
import cl.duoc.band_world.data.entities.SongWithFavorite
import cl.duoc.band_world.data.entities.SongWithRelations
import cl.duoc.band_world.data.entities.StatusEntity
import cl.duoc.band_world.data.entities.UserAlbumFavoriteEntity
import cl.duoc.band_world.data.entities.UserArtistFavoriteEntity
import cl.duoc.band_world.data.entities.UserBandFavoriteEntity
import cl.duoc.band_world.data.entities.UserEntity
import cl.duoc.band_world.data.entities.UserGenreFavoriteEntity
import cl.duoc.band_world.data.entities.UserSongFavoriteEntity
import cl.duoc.band_world.functions.AlbumWithSongs
import java.time.LocalDate


fun BandWithRelations.toDomain(
    context: Context,
    genres: List<GenreWithFavorite>?,
    members: List<MemberWithArtistsFavorite>?,
    albums: List<AlbumWithSongs>?
): Band {
    return Band(
        id = band.id,
        name = band.name,
        description = band.description,
        image = band.image?.toDrawableId(context),
        status = status.toDomain(),
        country = country.toDomain(),
        yearFounded = band.yearFounded,
        yearDisbanded = band.yearDisbanded,
        genres = genres?.map {
            Genre(
                id = it.genre.id,
                name = it.genre.name,
                isFavorite = it.isFavorite
            )
        } ?: emptyList(),
        members = members?.map {
            Member(
                id = it.member.id,
                artist = Artist(
                    id = it.artist.id,
                    name = it.artist.name,
                    description = it.artist.description,
                    image = it.artist.image?.toDrawableId(context),
                    birthDate = LocalDate.parse(it.artist.birthDate),
                    deathDate = it.artist.deathDate?.let { LocalDate.parse(it) } ,
                    country = Country(it.artist.countryId, ""),
                    isFavorite = it.isFavorite
                ),
                roles = it.roles.map { role ->
                    role.toDomain(context)
                },
                startYear = it.member.startYear,
                endYear = it.member.endYear
            )
        } ?: emptyList(),
        albums = albums?.map { albumWithSongs ->
            Album(
                id = albumWithSongs.album.album.id,
                name = albumWithSongs.album.album.name,
                description = albumWithSongs.album.album.description,
                image = albumWithSongs.album.album.id?.toDrawableId(context),
                releaseDate = LocalDate.parse(albumWithSongs.album.album.releaseDate),
                songs = albumWithSongs.songs.map { song ->
                    Song(
                        id = song.song.id,
                        number = song.song.number,
                        name = song.song.name,
                        isFavorite = song.isFavorite
                    )
                },
                isFavorite = albumWithSongs.album.isFavorite
            )
        },
        isFavorite = isFavorite
    )
}


fun AlbumEntity.toDomain(context: Context): Album {
    return Album(
        id = id,
        name = name,
        description = description,
        image = image?.toDrawableId(context),
        releaseDate = LocalDate.parse(releaseDate),
        songs = emptyList()
    )
}


fun AlbumWithRelations.toDomain(context: Context, songs: List<SongWithFavorite>? = null): Album {
    return Album(
        id = album.id,
        name = album.name,
        description = album.description,
        image = album.image?.toDrawableId(context),
        releaseDate = LocalDate.parse(album.releaseDate),
        songs = songs?.map { Song(
            id = it.song.id,
            number = it.song.number,
            name = it.song.name,
            isFavorite = it.isFavorite
        ) } ?: emptyList(),
        isFavorite = isFavorite
    )
}



fun SongEntity.toDomain(): Song {
    return Song(
        id = id,
        number = number,
        name = name,
    )
}
fun SongWithRelations.toDomain(context: Context): Song {
    return Song(
        id = song.id,
        number = song.number,
        name = song.name,
        isFavorite = isFavorite
    )
}

fun SongWithFavorite.toDomain(context: Context): Song {
    return Song(
        id = song.id,
        number = song.number,
        name = song.name,
        isFavorite = isFavorite
    )
}


fun StatusEntity.toDomain(): Status {
    return Status(
        id = id,
        name = name
    )
}


fun CountryEntity.toDomain(): Country {
    return Country(
        id = id,
        name = name,
        emoji = emoji
    )
}

fun GenreEntity.toDomain(): Genre {
    return Genre(
        id = id,
        name = name,
        // isFavorite = isFavorite
    )
}

fun GenreWithFavorite.toDomain(): Genre {
    return Genre(
        id = genre.id,
        name = genre.name,
        isFavorite = isFavorite
    )
}


fun MemberEntity.toDomain(context: Context): Member {
    return Member(
        id = id,
        artist = Artist(
            id = artistId,
            name = "",
            description = "",
            birthDate = LocalDate.now(),
            country = Country(id = "", name = "")
        ),
        startYear = startYear,
        endYear = endYear,
        roles = emptyList()
    )
}

//fun MemberWithRelations.toDomain(
//    context: Context,
//)
//: Member {
//    return Member(
//        id = member.id,
//        startYear = member.startYear,
//        endYear = member.endYear,
//        roles = roles.map { it.toDomain(context) },
//        artist = artist.toDomain(context),
//    )
//}



fun ArtistEntity.toDomain(
    context: Context
): Artist {
    return Artist(
        id = id,
        name = name,
        description = description,
        image = image?.toDrawableId(context),
        birthDate = LocalDate.parse(birthDate),
        deathDate = deathDate?.let { LocalDate.parse(it) },
        country = Country(
            id = countryId,
            name = ""
        )
    )
}


fun ArtistWithRelations.toDomain(context: Context): Artist {
    return Artist(
        id = artist.id,
        name = artist.name,
        description = artist.description,
        image = artist.image?.toDrawableId(context),
        birthDate = LocalDate.parse(artist.birthDate),
        deathDate = artist.deathDate?.let { LocalDate.parse(it) },
        country = country.toDomain(),
        isFavorite = isFavorite
    )
}

fun ArtistWithFavorite.toDomain(context: Context): Artist {
    return Artist(
        id = artist.id,
        name = artist.name,
        description = artist.description,
        image = artist.image?.toDrawableId(context),
        birthDate = LocalDate.parse(artist.birthDate),
        deathDate = artist.deathDate?.let { LocalDate.parse(it) },
        country = Country(id = artist.countryId, ""),
        isFavorite = isFavorite
    )
}


fun RoleEntity.toDomain(context: Context): Role {
    return Role(
        id = id,
        name = name,
        image = image?.toDrawableId(context)
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        name = name,
        username = username,
        email = email,
        password = password
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        username = username,
        email = email,
        password = password
    )
}

fun UserBandFavoriteEntity.toDomain(): UserBandFavorite {
    return UserBandFavorite(
        user_id = userId,
        band_id = bandId
    )
}

fun UserArtistFavoriteEntity.toDomain(): UserArtistFavorite {
    return UserArtistFavorite(
        user_id = userId,
        artist_id = artistId
    )
}

fun UserAlbumFavoriteEntity.toDomain(): UserAlbumFavorite {
    return UserAlbumFavorite(
        user_id = userId,
        album_id = albumId
    )
}

fun UserGenreFavoriteEntity.toDoamin(): UserGenreFavorite {
    return UserGenreFavorite(
        user_id = userId,
        genre_id = genreId
    )
}

fun UserSongFavoriteEntity.toDomain(): UserSongFavorite {
    return UserSongFavorite(
        user_id = userId,
        song_id = songId
    )
}

fun String.toDrawableId(context: Context): Int? {
    val id = context.resources.getIdentifier(
        this, "drawable", context.packageName
    )

    return if(id != 0) id else null
}

