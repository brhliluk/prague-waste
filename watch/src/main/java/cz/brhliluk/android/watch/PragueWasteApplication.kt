package cz.brhliluk.android.watch

import android.app.Application
import cz.brhliluk.android.praguewaste.common.api.WasteApi
import cz.brhliluk.android.praguewaste.common.utils.LocationHelper
import cz.brhliluk.android.praguewaste.common.utils.PreferencesManager
import cz.brhliluk.android.watch.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class PragueWasteApplication : Application() {

    val appModules = listOf(module {
        // ViewModel for Main View
        viewModel { MainViewModel() }
        single { WasteApi() }
        // Prefs
        single { PreferencesManager(get()) }
        // Location
        single { parameters -> LocationHelper(get(), parameters.get()) }
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