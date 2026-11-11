package plat.futurelabs.paiz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CharacterEntity::class,
        LocationEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun characterDao(): CharacterDao

    abstract fun locationDao(): LocationDao
}