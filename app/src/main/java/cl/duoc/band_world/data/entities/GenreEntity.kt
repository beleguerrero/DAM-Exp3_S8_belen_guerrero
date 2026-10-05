package cl.duoc.band_world.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre")
data class GenreEntity(
    @PrimaryKey
    val id: String = "",
    val name: String,
)

data class GenreWithFavorite(
    @Embedded
    val genre: GenreEntity,
    val isFavorite: Boolean
)

