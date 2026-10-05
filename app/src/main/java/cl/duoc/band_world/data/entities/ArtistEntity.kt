package cl.duoc.band_world.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey
    val id: String = "",
    val name: String,
    val description: String,
    val image: String? = null,
    val birthDate: String,
    val deathDate: String? = null,
    val countryId: String,
)

data class ArtistWithRelations(
    @Embedded
    val artist: ArtistEntity,

    @Relation(
        parentColumn = "countryId",
        entityColumn = "id"
    )
    val country: CountryEntity,

    val isFavorite: Boolean
)

data class ArtistWithFavorite(
    @Embedded
    val artist: ArtistEntity,

    @Relation(
        parentColumn = "countryId",
        entityColumn = "id"
    )
    val country: CountryEntity,

    val isFavorite: Boolean
)
