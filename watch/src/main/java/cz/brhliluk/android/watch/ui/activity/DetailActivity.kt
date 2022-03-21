package cz.brhliluk.android.watch.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.common.model.BinModel
import cz.brhliluk.android.watch.ui.activity.MainActivity.Companion.BIN_EXTRA
import cz.brhliluk.android.watch.ui.activity.MainActivity.Companion.LOCATION_EXTRA
import cz.brhliluk.android.watch.ui.theme.PragueWasteTheme
import cz.brhliluk.android.watch.ui.view.BinView

class DetailActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bin = intent.extras?.get(BIN_EXTRA)
        val userLocation = intent.extras?.get(LOCATION_EXTRA)
        setContent {
            PragueWasteTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    BinView(bin = (bin as BinModel).toBin(), userLocation = userLocation as LatLng)
                }
            }
        }
    }
}