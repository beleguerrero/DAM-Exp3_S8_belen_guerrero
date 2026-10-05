package cl.duoc.band_world.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import cl.duoc.band_world.data.Country


@Entity(tableName = "member")
data class MemberEntity(
    @PrimaryKey
    val id: String = "",
    val bandId: String,
    val artistId: String,
    val startYear: Int,
    val endYear: Int? = null,
)

@Entity(
    tableName = "member_role",
    primaryKeys = ["memberId", "roleId"]
)
data class MemberRoleCrossRef(
    val memberId: String,
    val roleId: String
)


data class MemberWithRelations(
    @Embedded
    val member: MemberEntity,

    @Relation(
        parentColumn = "bandId",
        entityColumn = "id"
    )
    val band: BandEntity,

    @Relation(
        parentColumn = "artistId",
        entityColumn = "id"
    )
    val artist: ArtistEntity,


    @Relation(
        entity = RoleEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MemberRoleCrossRef::class,
            parentColumn = "memberId",
            entityColumn = "roleId"
        )
    )
    val roles: List<RoleEntity>,

)



data class MemberWithArtistsFavorite(
    @Embedded
    val member: MemberEntity,

    @Relation(
        parentColumn = "artistId",
        entityColumn = "id"
    )
    val artist: ArtistEntity,

//    @Relation(
//        parentColumn = "artistId",
//        entityColumn = "id"
//    )
//    val country: CountryEntity,

    @Relation(
        entity = RoleEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MemberRoleCrossRef::class,
            parentColumn = "memberId",
            entityColumn = "roleId"
        )
    )
    val roles: List<RoleEntity>,

    val isFavorite: Boolean
)

