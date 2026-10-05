package cl.duoc.band_world.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "song")
data class SongEntity(
    @PrimaryKey
    val id: String = "",
    val albumId: String,
    val number: Int,
    val name: String
)

data class SongWithRelations(
    @Embedded
    val song: SongEntity,

//    @Relation(
//        parentColumn = "albumId",
//        entityColumn = "id"
//    )
//    val album: AlbumEntity,

    val isFavorite: Boolean
)


data class SongWithFavorite(
    @Embedded
    val song: SongEntity,
    val isFavorite: Boolean
)
