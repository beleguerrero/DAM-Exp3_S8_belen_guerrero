package cl.duoc.band_world.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "user_song_favorites",
    primaryKeys = ["userId", "songId"],
    indices = [
        Index(value = ["songId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserSongFavoriteEntity(
    val userId: String,
    val songId: String
)


