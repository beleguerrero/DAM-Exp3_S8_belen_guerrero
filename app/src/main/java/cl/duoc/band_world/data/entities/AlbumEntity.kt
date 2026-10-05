package cl.duoc.band_world.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "album")
data class AlbumEntity(
    @PrimaryKey
    val id: String = "",
    val bandId: String,
    val name: String,
    val description: String,
    val image: String? = null,
    val releaseDate: String,
)


data class AlbumWithRelations(
    @Embedded
    val album: AlbumEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val songs: List<SongEntity>,

    val isFavorite: Boolean

)

data class AlbumWithFavorite(
    @Embedded
    val album: AlbumEntity,

//    val songs: List<SongWithFavorite>,

    val isFavorite: Boolean
)
