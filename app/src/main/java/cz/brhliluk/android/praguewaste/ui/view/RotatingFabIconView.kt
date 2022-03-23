package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cz.brhliluk.android.praguewaste.ui.theme.PaperBlue
import dev.burnoo.compose.rememberpreference.rememberBooleanPreference
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun RotatingFabIconView(modifier: Modifier, onClick: () -> Unit) {
    var clicked by rememberBooleanPreference(keyName = "filterClicked", initialValue = false, defaultValue = false)
    val rotationAnimationFloat = remember { Animatable(0f) }

    // Notify user about the existence of filter button
    LaunchedEffect(rotationAnimationFloat) {
        while (!clicked) {
            delay(10.seconds.inWholeMilliseconds)
            rotationAnimationFloat.animateTo(
                targetValue = 360F,
                animationSpec = tween(800, easing = LinearEasing)
            )
            rotationAnimationFloat.snapTo(0f)
        }
    }

    FloatingActionButton(
        modifier = Modifier
            .padding(top = 90.dp, end = 12.dp)
            .size(40.dp)
            .then(modifier),
        onClick = {
            clicked = true
            onClick()
        },
        backgroundColor = PaperBlue,
        contentColor = Color.White
    ) {
        Icon(Icons.Filled.FilterAlt, "Filter icon",
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = rotationAnimationFloat.value
                })
    }
}