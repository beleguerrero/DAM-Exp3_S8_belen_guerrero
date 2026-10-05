package cl.duoc.band_world.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "status")
data class StatusEntity(
    @PrimaryKey
    val id: String = "",
    val name: String
)

