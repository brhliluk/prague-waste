package cz.brhliluk.android.praguewaste

import android.app.Application
import androidx.room.Room
import cz.brhliluk.android.praguewaste.common.api.WasteApi
import cz.brhliluk.android.praguewaste.common.database.BinDatabase
import cz.brhliluk.android.praguewaste.common.repository.BinRepository
import cz.brhliluk.android.praguewaste.common.utils.LocationHelper
import cz.brhliluk.android.praguewaste.utils.InfoWindowAdapter
import cz.brhliluk.android.praguewaste.common.utils.PreferencesManager
import cz.brhliluk.android.praguewaste.utils.GoogleMapsHelper
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import cz.brhliluk.android.praguewaste.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class PragueWasteApplication : Application() {

    val appModules = listOf(module {
        // ViewModels
        viewModel { MainViewModel() }
        viewModel { SettingsViewModel() }
        // API
        single { WasteApi() }
        // Room Database
        single {
            Room.databaseBuilder(androidApplication(), BinDatabase::class.java, "bin-db")
                .fallbackToDestructiveMigration().build()
        }
        // DAOs
        single { get<BinDatabase>().binDao() }
        single { get<BinDatabase>().binFtsDao() }
        // Repository
        single { BinRepository(binDao = get(), binFtsDao = get()) }
        // Prefs
        single { PreferencesManager(get()) }
        // Google maps
        single { InfoWindowAdapter(get()) }
        single { GoogleMapsHelper(get()) }
        // Location helper
        single { params -> LocationHelper(get(), params.get()) }
    })

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@PragueWasteApplication)
            modules(appModules)
        }

    }

}