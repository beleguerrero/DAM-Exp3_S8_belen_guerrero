package cl.duoc.band_world.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "band")
data class BandEntity(
    @PrimaryKey
    val id: String = "",
    val name: String,
    val description: String,
    val image: String? = null,
    val statusId: String,
    val countryId: String,
    val yearFounded: Int,
    val yearDisbanded: Int? = null,
)

@Entity(
    tableName = "band_genre",
    primaryKeys = ["bandId", "genreId"]
)
data class BandGenreCrossRef(
    val bandId: String,
    val genreId: String
)


data class BandWithRelations(
    @Embedded
    val band: BandEntity,

    @Relation(
        parentColumn = "statusId",
        entityColumn = "id"
    )
    val  status: StatusEntity,

    @Relation(
        parentColumn = "countryId",
        entityColumn = "id"
    )
    val country: CountryEntity,

    @Relation(
        entity = GenreEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = BandGenreCrossRef::class,
            parentColumn = "bandId",
            entityColumn = "genreId"
        )
    )
    val genres: List<GenreEntity>,

    @Relation(
        entity = MemberEntity::class,
        parentColumn = "id",
        entityColumn = "bandId"
    )
    val members: List<MemberEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "bandId"
    )
    val albums: List<AlbumEntity>,

    val isFavorite: Boolean

)

