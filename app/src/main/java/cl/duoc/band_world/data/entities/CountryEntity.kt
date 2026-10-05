package cl.duoc.band_world.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
data class CountryEntity(
    @PrimaryKey
    val id: String = "",
    val name: String,
    val emoji: String? = null
)

