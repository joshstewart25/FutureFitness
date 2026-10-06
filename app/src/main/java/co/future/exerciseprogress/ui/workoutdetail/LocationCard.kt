package co.future.exerciseprogress.ui.workoutdetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import co.future.exerciseprogress.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

private val MAP_HEIGHT = 200.dp
private const val MAP_ZOOM_LEVEL = 15.0

@Composable
fun LocationCard(
    location: LocationUiState,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.workout_location),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            LocationMap(
                location = location,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MAP_HEIGHT)
            )
        }
    }
}

// An OpenStreetMap view with a pin on the workout location. It is a picture only, so it can't be dragged or zoomed.
@Composable
private fun LocationMap(
    location: LocationUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val mapView = remember { createMapView(context) }
    val mapDescription = stringResource(R.string.workout_location_map_description)
    val pinIcon = remember { ContextCompat.getDrawable(context, R.drawable.ic_map_pin) }

    // osmdroid needs to be told when the screen pauses and resumes, and to let go of its resources when it leaves.
    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    AndroidView(
        factory = { mapView },
        update = { it.showPin(location, pinIcon) },
        // The map paints a little past its edges, so it is clipped to stay off the card's header.
        modifier = modifier
            .clipToBounds()
            .semantics { contentDescription = mapDescription }
    )
}

private fun createMapView(context: Context): MapView {
    // OpenStreetMap asks apps to identify themselves. The downloaded map tiles are kept in the app's own cache folder.
    Configuration.getInstance().apply {
        userAgentValue = context.packageName
        osmdroidBasePath = File(context.cacheDir, "osmdroid")
        osmdroidTileCache = File(context.cacheDir, "osmdroid/tiles")
    }

    return StaticMapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        controller.setZoom(MAP_ZOOM_LEVEL)
    }
}

private fun MapView.showPin(location: LocationUiState, pinIcon: Drawable?) {
    val position = GeoPoint(location.latitude, location.longitude)

    val pin = Marker(this).apply {
        this.position = position
        icon = pinIcon
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        setInfoWindow(null)
    }
    overlays.clear()
    overlays.add(pin)
    controller.setCenter(position)
}

// Ignores touches so the screen keeps scrolling when a finger lands on the map.
private class StaticMapView(context: Context) : MapView(context) {
    override fun dispatchTouchEvent(event: MotionEvent): Boolean = false
}
