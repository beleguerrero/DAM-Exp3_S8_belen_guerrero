package cl.duoc.band_world.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "role")
data class RoleEntity(
    @PrimaryKey
    val id: String = "",
    val name: String,
    val image: String? = null
)


