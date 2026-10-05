package cl.duoc.band_world.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "user_band_favorites",
    primaryKeys = ["userId", "bandId"],
    indices = [
        Index(value = ["bandId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BandEntity::class,
            parentColumns = ["id"],
            childColumns = ["bandId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserBandFavoriteEntity(
    val userId: String,
    val bandId: String
)

