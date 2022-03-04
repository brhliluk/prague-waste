package cz.brhliluk.android.praguewaste.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.brhliluk.android.praguewaste.model.Bin
import org.koin.core.component.KoinComponent

@Database(entities = [Bin::class], version = 1)
abstract class BinDatabase : RoomDatabase(), KoinComponent {
    abstract fun binDao(): BinDao
}