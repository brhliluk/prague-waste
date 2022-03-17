package cz.brhliluk.android.praguewaste.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.brhliluk.android.praguewaste.common.model.*
import org.koin.core.component.KoinComponent

@Database(entities = [BinEntity::class, BinFts::class], version = 3,)
@TypeConverters(Converters::class)
abstract class BinDatabase : RoomDatabase(), KoinComponent {
    abstract fun binDao(): BinDao
    abstract fun binFtsDao(): BinFtsDao
}