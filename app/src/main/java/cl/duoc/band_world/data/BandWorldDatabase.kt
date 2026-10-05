package cl.duoc.band_world.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.band_world.data.dao.BandDao
import cl.duoc.band_world.data.dao.UserDao
import cl.duoc.band_world.data.entities.AlbumEntity
import cl.duoc.band_world.data.entities.ArtistEntity
import cl.duoc.band_world.data.entities.BandEntity
import cl.duoc.band_world.data.entities.BandGenreCrossRef
import cl.duoc.band_world.data.entities.CountryEntity
import cl.duoc.band_world.data.entities.GenreEntity
import cl.duoc.band_world.data.entities.MemberEntity
import cl.duoc.band_world.data.entities.MemberRoleCrossRef
import cl.duoc.band_world.data.entities.RoleEntity
import cl.duoc.band_world.data.entities.SongEntity
import cl.duoc.band_world.data.entities.StatusEntity
import cl.duoc.band_world.data.entities.UserAlbumFavoriteEntity
import cl.duoc.band_world.data.entities.UserArtistFavoriteEntity
import cl.duoc.band_world.data.entities.UserBandFavoriteEntity
import cl.duoc.band_world.data.entities.UserEntity
import cl.duoc.band_world.data.entities.UserGenreFavoriteEntity
import cl.duoc.band_world.data.entities.UserSongFavoriteEntity

@Database(
    entities = [
        BandEntity::class,
        BandGenreCrossRef::class,
        AlbumEntity::class,
        ArtistEntity::class,
        CountryEntity::class,
        GenreEntity::class,
        MemberEntity::class,
        MemberRoleCrossRef::class,
        RoleEntity::class,
        SongEntity::class,
        StatusEntity::class,

        UserEntity::class,
        UserAlbumFavoriteEntity::class,
        UserArtistFavoriteEntity::class,
        UserBandFavoriteEntity::class,
        UserGenreFavoriteEntity::class,
        UserSongFavoriteEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class  BandWorldDatabase : RoomDatabase() {
    abstract fun bandDao(): BandDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: BandWorldDatabase? = null

        fun getDatabase(context: Context): BandWorldDatabase {
            return  INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BandWorldDatabase::class.java,
                    "bandworld.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}




