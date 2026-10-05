package cl.duoc.band_world.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: String = "",
    val name: String,
    val username: String,
    val email: String,
    val password: String
)


