package cz.brhliluk.android.praguewaste.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.model.BinFts
import cz.brhliluk.android.praguewaste.model.Converters
import org.koin.core.component.KoinComponent

@Database(entities = [Bin::class, BinFts::class], version = 3,)
@TypeConverters(Converters::class)
abstract class BinDatabase : RoomDatabase(), KoinComponent {
    abstract fun binDao(): BinDao
    abstract fun binFtsDao(): BinFtsDao
}