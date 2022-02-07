package cz.brhliluk.android.praguewaste

import android.app.Application
import cz.brhliluk.android.praguewaste.api.WasteApi
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class PragueWasteApplication: Application() {

    val appModules = listOf(module {
        // ViewModel for Detail View
        viewModel { MainViewModel() }
        single { WasteApi() }
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