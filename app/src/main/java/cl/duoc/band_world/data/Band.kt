package cl.duoc.band_world.data

import cl.duoc.band_world.R
import java.time.LocalDate
import java.util.UUID

data class Band(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val image: Int? = null,
    val status: Status,
    val country: Country,
    val yearFounded: Int,
    val yearDisbanded: Int? = null,
    val genres: List<Genre>,
    val members: List<Member>,
    val albums: List<Album>? = null,
    val isFavorite: Boolean = false
)

data class Status(
    val id: String = UUID.randomUUID().toString(),
    val name: String
)

data class Country(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val emoji: String? = null
)


data class Genre(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isFavorite: Boolean = false
)


data class Member(
    val id: String = UUID.randomUUID().toString(),
    val artist: Artist,
    val startYear: Int,
    val endYear: Int? = null,
    val roles: List<Role>,
)

data class Artist(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val image: Int? = null,
    val birthDate: LocalDate,
    val deathDate: LocalDate? = null,
    val country: Country,
    val isFavorite: Boolean = false
)

data class Role(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val image: Int? = null
)


data class Album(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val image: Int? = null,
    val releaseDate: LocalDate,
    val songs: List<Song>,
    val isFavorite: Boolean = false
)


data class Song(
    val id: String = UUID.randomUUID().toString(),
    val number: Int,
    val name: String,
    val isFavorite: Boolean = false
)

data class Names(
    val id: String,
    val name: String
)

