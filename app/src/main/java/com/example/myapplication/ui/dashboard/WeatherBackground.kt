package com.weathermixer.sixq

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.graphics.Paint as AndroidPaint
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedImageDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.Looper
import android.os.VibrationEffect
import android.os.VibrationAttributes
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.ImageView
import androidx.activity.BackEventCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable as ComposeAnimatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import java.util.Locale
import java.util.SimpleTimeZone
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.coroutines.resume

internal data class GeometricCloud(
    val xFraction: Float,
    val yFraction: Float,
    val scaleFraction: Float,
    val nearLayer: Boolean,
)

@Composable
internal fun WeatherSceneBackground(
    condition: WeatherCondition,
    alertLevel: AlertLevel,
    isNight: Boolean,
    animationEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val sceneCondition = if (alertLevel == AlertLevel.Severe) WeatherCondition.Storm else condition
    val sceneFamily = sceneCondition.visualFamily
    val appDarkTheme = LocalAppDarkTheme.current
    val context = LocalContext.current
    var tiltX by remember(sceneCondition, isNight) { mutableFloatStateOf(0f) }
    var tiltY by remember(sceneCondition, isNight) { mutableFloatStateOf(0f) }
    val gradientColors = remember(sceneCondition, isNight) {
        if (isNight) {
            when (sceneFamily) {
                WeatherVisualFamily.Clear -> listOf(Color(0xFF061322), Color(0xFF102A45), Color(0xFF234865))
                WeatherVisualFamily.Cloud -> listOf(Color(0xFF0B1422), Color(0xFF22354A), Color(0xFF45596C))
                WeatherVisualFamily.Rain -> listOf(Color(0xFF07131F), Color(0xFF193249), Color(0xFF36536A))
                WeatherVisualFamily.Thunder -> listOf(Color(0xFF030812), Color(0xFF111C2E), Color(0xFF29384B))
                WeatherVisualFamily.Snow -> listOf(Color(0xFF101B2B), Color(0xFF29445E), Color(0xFF60798E))
                WeatherVisualFamily.Atmosphere -> listOf(Color(0xFF151D22), Color(0xFF35464B), Color(0xFF697778))
                WeatherVisualFamily.Dust -> listOf(Color(0xFF211912), Color(0xFF4A3828), Color(0xFF776148))
            }
        } else {
            when (sceneFamily) {
                WeatherVisualFamily.Clear -> listOf(Color(0xFF7BD6FF), Color(0xFFC9F3FF), Color(0xFFFFE6AA))
                WeatherVisualFamily.Cloud -> listOf(Color(0xFF82AFCB), Color(0xFFC4D4E0), Color(0xFFE8EEF3))
                WeatherVisualFamily.Rain -> listOf(Color(0xFF375E7D), Color(0xFF6E8EA8), Color(0xFFD2DEE8))
                WeatherVisualFamily.Thunder -> listOf(Color(0xFF14263D), Color(0xFF405A75), Color(0xFF879AAD))
                WeatherVisualFamily.Snow -> listOf(Color(0xFF8DB9D4), Color(0xFFD6EBF3), Color(0xFFF7FBFC))
                WeatherVisualFamily.Atmosphere -> listOf(Color(0xFF8C9B9C), Color(0xFFC7D0CC), Color(0xFFE8ECE7))
                WeatherVisualFamily.Dust -> listOf(Color(0xFFC39A64), Color(0xFFE1C494), Color(0xFFF2DEB3))
            }
        }
    }
    val cloudTint = remember(sceneCondition, isNight) {
        if (isNight) {
            when (sceneFamily) {
                WeatherVisualFamily.Clear -> Color(0xFFBFD7ED).copy(alpha = 0.25f)
                WeatherVisualFamily.Cloud -> Color(0xFFC5D5E5).copy(alpha = 0.42f)
                WeatherVisualFamily.Rain -> Color(0xFFAFC6D8).copy(alpha = 0.38f)
                WeatherVisualFamily.Thunder -> Color(0xFF90A5BB).copy(alpha = 0.34f)
                WeatherVisualFamily.Snow -> Color(0xFFDCEBFA).copy(alpha = 0.40f)
                WeatherVisualFamily.Atmosphere -> Color(0xFFC7D3D1).copy(alpha = 0.32f)
                WeatherVisualFamily.Dust -> Color(0xFFC8A77A).copy(alpha = 0.30f)
            }
        } else {
            when (sceneFamily) {
                WeatherVisualFamily.Clear -> Color.White.copy(alpha = 0.54f)
                WeatherVisualFamily.Cloud -> Color.White.copy(alpha = 0.72f)
                WeatherVisualFamily.Rain -> Color(0xFFE4EDF5).copy(alpha = 0.62f)
                WeatherVisualFamily.Thunder -> Color(0xFFB9C4D1).copy(alpha = 0.58f)
                WeatherVisualFamily.Snow -> Color.White.copy(alpha = 0.76f)
                WeatherVisualFamily.Atmosphere -> Color(0xFFF1F3EE).copy(alpha = 0.48f)
                WeatherVisualFamily.Dust -> Color(0xFFE8CAA0).copy(alpha = 0.42f)
            }
        }
    }
    val cloudyClouds = remember(sceneCondition) {
        if (sceneFamily != WeatherVisualFamily.Cloud) {
            emptyList()
        } else {
            val random = Random(System.nanoTime())
            List(4) { index ->
                GeometricCloud(
                    xFraction = random.nextFloat() * 0.78f - 0.08f,
                    yFraction = 0.08f + random.nextFloat() * 0.43f,
                    scaleFraction = 0.21f + random.nextFloat() * 0.09f,
                    nearLayer = index % 2 == 0,
                )
            }.sortedBy(GeometricCloud::yFraction)
        }
    }

    DisposableEffect(context, animationEnabled, sceneCondition, isNight) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val gravitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val listener = object : SensorEventListener {
            var lastFrameNanos = 0L

            override fun onSensorChanged(event: SensorEvent) {
                if (event.timestamp - lastFrameNanos < 8_000_000L) return
                lastFrameNanos = event.timestamp
                val nextX = (-event.values[0] / SensorManager.GRAVITY_EARTH).coerceIn(-1f, 1f)
                val nextY = (event.values[1] / SensorManager.GRAVITY_EARTH).coerceIn(-1f, 1f)
                tiltX = tiltX * 0.64f + nextX * 0.36f
                tiltY = tiltY * 0.64f + nextY * 0.36f
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        if (animationEnabled && gravitySensor != null) {
            sensorManager.registerListener(listener, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
        onDispose { sensorManager?.unregisterListener(listener) }
    }

    Canvas(modifier = modifier) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = gradientColors,
                startY = 0f,
                endY = size.height,
            )
        )
        if (appDarkTheme) {
            drawRect(Color.Black.copy(alpha = 0.14f))
        }

        val farShift = Offset(tiltX * size.width * 0.034f, tiltY * size.height * 0.024f)
        val nearShift = Offset(tiltX * size.width * 0.092f, tiltY * size.height * 0.062f)

        if (sceneFamily == WeatherVisualFamily.Clear) {
            val celestialCenter = Offset(size.width * 0.78f, size.height * 0.16f) + farShift
            if (isNight) {
                drawCircle(
                    color = Color(0xFF9FC9FF).copy(alpha = 0.16f),
                    radius = size.minDimension * 0.20f,
                    center = celestialCenter,
                )
                drawCircle(
                    color = Color(0xFFDCEBFF),
                    radius = size.minDimension * 0.072f,
                    center = celestialCenter,
                )
                drawCircle(
                    color = Color(0xFFB9D2EE).copy(alpha = 0.72f),
                    radius = size.minDimension * 0.014f,
                    center = celestialCenter + Offset(-18f, 12f),
                )
                drawCircle(
                    color = Color(0xFFB9D2EE).copy(alpha = 0.56f),
                    radius = size.minDimension * 0.009f,
                    center = celestialCenter + Offset(15f, -17f),
                )
            } else {
                drawCircle(
                    color = Color(0xFFFFCF5A).copy(alpha = 0.32f),
                    radius = size.minDimension * 0.22f,
                    center = celestialCenter,
                )
                drawCircle(
                    color = Color(0xFFFFC857),
                    radius = size.minDimension * 0.09f,
                    center = celestialCenter,
                )
            }
        }

        fun drawGeometricCloud(baseX: Float, baseY: Float, scale: Float) {
            drawCircle(cloudTint, radius = scale * 0.52f, center = Offset(baseX + scale * 0.8f, baseY))
            drawCircle(cloudTint.copy(alpha = cloudTint.alpha * 0.9f), radius = scale * 0.42f, center = Offset(baseX + scale * 0.35f, baseY + scale * 0.12f))
            drawCircle(cloudTint.copy(alpha = cloudTint.alpha * 0.82f), radius = scale * 0.48f, center = Offset(baseX + scale * 1.26f, baseY + scale * 0.16f))
            drawRoundRect(
                color = cloudTint.copy(alpha = cloudTint.alpha * 0.82f),
                topLeft = Offset(baseX + scale * 0.18f, baseY + scale * 0.12f),
                size = Size(scale * 1.48f, scale * 0.48f),
                cornerRadius = CornerRadius(scale * 0.28f, scale * 0.28f),
            )
        }
        if (sceneFamily == WeatherVisualFamily.Cloud) {
            cloudyClouds.forEach { cloud ->
                val layerShift = if (cloud.nearLayer) nearShift else farShift
                drawGeometricCloud(
                    baseX = size.width * cloud.xFraction + layerShift.x,
                    baseY = size.height * cloud.yFraction + layerShift.y,
                    scale = size.minDimension * cloud.scaleFraction,
                )
            }
        } else {
            repeat(if (sceneFamily == WeatherVisualFamily.Clear) 3 else 5) { index ->
                val layerShift = if (index % 2 == 0) farShift else nearShift
                drawGeometricCloud(
                    baseX = size.width * (0.08f + index * 0.22f) + layerShift.x,
                    baseY = size.height * (0.12f + (index % 4) * 0.12f) + layerShift.y,
                    scale = size.minDimension * (0.11f + (index % 3) * 0.018f),
                )
            }
        }

        if (sceneFamily == WeatherVisualFamily.Rain || sceneFamily == WeatherVisualFamily.Thunder) {
            val isThunder = sceneFamily == WeatherVisualFamily.Thunder
            val rainCount = if (isThunder) 44 else 28
            val rainColor = if (isThunder) Color(0xFFA9D8FF) else Color(0xFF2D82C7)
            repeat(rainCount) { index ->
                val lane = (index * 37 % rainCount) / rainCount.toFloat()
                val x = size.width * lane + nearShift.x
                val y = size.height * (0.24f + (index * 53 % rainCount) / rainCount.toFloat() * 0.72f) + nearShift.y
                val length = if (isThunder) 38f else 24f
                drawLine(
                    color = rainColor.copy(alpha = if (isThunder) 0.46f else 0.34f),
                    start = Offset(x, y),
                    end = Offset(x - length * 0.35f, y + length),
                    strokeWidth = if (isThunder) 3.6f else 2.6f,
                    cap = StrokeCap.Round,
                )
            }
        }

        if (sceneFamily == WeatherVisualFamily.Snow) {
            repeat(32) { index ->
                val x = size.width * ((index * 47 % 101) / 101f) + nearShift.x
                val y = size.height * (0.18f + (index * 61 % 79) / 79f * 0.76f) + nearShift.y
                val radius = 2.2f + (index % 3) * 1.1f
                drawCircle(Color.White.copy(alpha = 0.72f), radius = radius, center = Offset(x, y))
            }
        }

        if (sceneFamily == WeatherVisualFamily.Atmosphere) {
            repeat(7) { index ->
                val y = size.height * (0.28f + index * 0.075f) + nearShift.y * 0.35f
                val inset = if (index % 2 == 0) size.width * 0.08f else size.width * 0.17f
                drawLine(
                    color = Color.White.copy(alpha = 0.16f + index * 0.012f),
                    start = Offset(inset + farShift.x, y),
                    end = Offset(size.width - inset + nearShift.x, y),
                    strokeWidth = 9f,
                    cap = StrokeCap.Round,
                )
            }
        }

        if (sceneFamily == WeatherVisualFamily.Dust) {
            repeat(if (sceneCondition == WeatherCondition.Sandstorm) 46 else 25) { index ->
                val x = size.width * ((index * 43 % 97) / 97f) + nearShift.x
                val y = size.height * (0.20f + (index * 29 % 73) / 73f * 0.70f) + nearShift.y
                drawCircle(
                    color = Color(0xFF7D5429).copy(alpha = if (isNight) 0.30f else 0.25f),
                    radius = 1.8f + (index % 4) * 0.75f,
                    center = Offset(x, y),
                )
            }
        }

        if (sceneFamily == WeatherVisualFamily.Thunder) {
            val start = Offset(size.width * 0.68f, size.height * 0.14f) + nearShift
            drawLine(Color(0xFFFFF3A3).copy(alpha = 0.82f), start, Offset(size.width * 0.56f, size.height * 0.36f) + nearShift, strokeWidth = 7f, cap = StrokeCap.Round)
            drawLine(Color(0xFFFFF3A3).copy(alpha = 0.82f), Offset(size.width * 0.56f, size.height * 0.36f) + nearShift, Offset(size.width * 0.66f, size.height * 0.34f) + nearShift, strokeWidth = 7f, cap = StrokeCap.Round)
            drawLine(Color(0xFFFFF3A3).copy(alpha = 0.82f), Offset(size.width * 0.66f, size.height * 0.34f) + nearShift, Offset(size.width * 0.49f, size.height * 0.62f) + nearShift, strokeWidth = 7f, cap = StrokeCap.Round)
        }
    }
}

@Composable
internal fun AnimatedWeatherHeader(title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .floatingCardMotion(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun LocationSearchControl(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    compactLabel: String,
    temperatureText: String,
    headerProgress: Float,
    onCompactTemperaturePositioned: (Offset) -> Unit,
    predictiveBackProgress: Float,
    predictiveBackDirection: Float,
    refreshLabel: String,
    refreshSecondaryLabel: String?,
    refreshLabelColor: Color,
    onExpandedChange: (Boolean) -> Unit,
    selectedRegion: District,
    locationMethod: LocationMethod,
    locationMessage: String?,
    citySearchResults: List<District>,
    savedRegions: List<District>,
    citySearchMessage: String?,
    isCitySearching: Boolean,
    onRegionSelected: (District) -> Unit,
    onSavedRegionRemoved: (District) -> Unit,
    onCitySearch: (String) -> Unit,
    onLocationMethodChanged: (LocationMethod) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    val darkTheme = LocalAppDarkTheme.current
    var searchQuery by remember { mutableStateOf("") }
    var resultsExpanded by remember { mutableStateOf(false) }
    var skipNextAutoSearch by remember { mutableStateOf(false) }
    var lastSubmittedQuery by remember { mutableStateOf("") }
    val visibleResults = citySearchResults.take(8)
    val controlShape = RoundedCornerShape(20.dp)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) onUseCurrentLocation() else onPermissionDenied()
    }

    fun locateCurrentCity() {
        if (hapticFeedbackEnabled) {
            context.performAppVibration(AppVibration.StrongImpact)
        }
        onExpandedChange(false)
        resultsExpanded = false
        focusManager.clearFocus()
        if (locationMethod == LocationMethod.BaiduIp || hasLocationPermission(context)) {
            onUseCurrentLocation()
        } else {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    LaunchedEffect(searchQuery, expanded) {
        val keyword = searchQuery.trim()
        if (!expanded) {
            return@LaunchedEffect
        }
        if (skipNextAutoSearch) {
            skipNextAutoSearch = false
            return@LaunchedEffect
        }
        if (keyword.isBlank()) {
            resultsExpanded = false
            lastSubmittedQuery = ""
            return@LaunchedEffect
        }
        delay(420)
        resultsExpanded = true
        if (keyword != lastSubmittedQuery) {
            lastSubmittedQuery = keyword
            onCitySearch(keyword)
        }
    }

    BoxWithConstraints(
        modifier = modifier.graphicsLayer {
            translationX = size.width * predictiveBackProgress * 0.18f * predictiveBackDirection
            scaleX = 1f - predictiveBackProgress * 0.012f
            scaleY = 1f - predictiveBackProgress * 0.012f
            alpha = 1f - predictiveBackProgress * 0.18f
            transformOrigin = TransformOrigin.Center
        },
    ) {
        val dockProgress = headerProgress.coerceIn(0f, 1f)
        val compactWidth = (
            62f + compactLabel.length.coerceAtMost(7) * 14f +
                (1f - dockProgress) * 14f + dockProgress * 68f
            ).dp
            .coerceAtMost(238.dp)
            .coerceAtMost(maxWidth)
        val expansionProgress by animateFloatAsState(
            targetValue = if (expanded) 1f else 0f,
            animationSpec = tween(460, easing = FastOutSlowInEasing),
            label = "locationControlExpansion",
        )
        val expandedContentAlpha by animateFloatAsState(
            targetValue = if (expanded && expansionProgress >= 0.995f) 1f else 0f,
            animationSpec = if (expanded) {
                tween(220, easing = FastOutSlowInEasing)
            } else {
                tween(90, easing = FastOutSlowInEasing)
            },
            label = "locationExpandedContentAlpha",
        )
        val controlWidth = compactWidth + (maxWidth - compactWidth) * expansionProgress
        val expandedContentWidth = maxWidth
        val animatedShape = 24.dp + 4.dp * expansionProgress
        val panelElevation by animateDpAsState(
            targetValue = if (expanded) 8.dp else 0.dp,
            animationSpec = if (expanded) tween(180, delayMillis = 300) else tween(220, easing = FastOutSlowInEasing),
            label = "locationPanelElevation",
        )

        Box {
            Card(
                modifier = Modifier
                    .width(controlWidth)
                    .floatingCardMotion()
                    .clip(RoundedCornerShape(animatedShape))
                    .clickable(enabled = !expanded) {
                        if (hapticFeedbackEnabled) {
                            context.performAppVibration(AppVibration.StrongImpact)
                        }
                        onExpandedChange(true)
                        resultsExpanded = false
                    },
                shape = RoundedCornerShape(animatedShape),
                colors = CardDefaults.cardColors(
                    containerColor = if (darkTheme) {
                        MaterialTheme.colorScheme.surface.copy(alpha = if (expanded) 0.96f else 0.72f)
                    } else {
                        Color.White.copy(alpha = if (expanded) 0.94f else 0.62f)
                    },
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = panelElevation),
            ) {
                AnimatedContent(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start, unbounded = true)
                        .requiredWidth(expandedContentWidth),
                    targetState = expanded,
                    transitionSpec = {
                        val contentTransition = if (targetState) {
                            fadeIn(tween(360, easing = FastOutSlowInEasing)) togetherWith
                                fadeOut(tween(180, easing = FastOutSlowInEasing))
                        } else {
                            fadeIn(tween(90, easing = FastOutSlowInEasing)) togetherWith
                                fadeOut(tween(220, delayMillis = 80, easing = FastOutSlowInEasing))
                        }
                        contentTransition.using(
                            SizeTransform(clip = true) { _, _ ->
                                tween(460, easing = FastOutSlowInEasing)
                            }
                        )
                    },
                    label = "locationSearchContent",
                ) { isExpanded ->
                    if (!isExpanded) {
                        Row(
                            modifier = Modifier
                                .height(48.dp)
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    AnimatedContent(
                        targetState = compactLabel,
                        transitionSpec = {
                            (fadeIn(tween(300)) + slideInVertically { it / 2 }) togetherWith
                                (fadeOut(tween(220)) + slideOutVertically { -it / 2 })
                        },
                        label = "compactWeatherHeader",
                    ) { label ->
                        Text(
                            text = label,
                            modifier = Modifier
                                .widthIn(max = 96.dp)
                                .offset(y = 0.dp)
                                .then(
                                    if (label.length > 6) {
                                        Modifier
                                            .graphicsLayer {
                                                compositingStrategy = CompositingStrategy.Offscreen
                                            }
                                            .drawWithContent {
                                                drawContent()
                                                val edgeFraction = (12.dp.toPx() / size.width).coerceIn(0f, 0.32f)
                                                drawRect(
                                                    brush = Brush.horizontalGradient(
                                                        0f to Color.Transparent,
                                                        edgeFraction to Color.Black,
                                                        (1f - edgeFraction) to Color.Black,
                                                        1f to Color.Transparent,
                                                    ),
                                                    blendMode = BlendMode.DstIn,
                                                )
                                            }
                                    } else {
                                        Modifier
                                    }
                                )
                                .basicMarquee(iterations = Int.MAX_VALUE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (darkTheme) MaterialTheme.colorScheme.onSurface else Color.Black,
                            maxLines = 1,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width((13f * dockProgress).dp)
                            .height(26.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = " · ",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = dockProgress),
                            maxLines = 1,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width((55f * dockProgress).dp)
                            .height(26.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = temperatureText,
                            modifier = Modifier.onGloballyPositioned {
                                onCompactTemperaturePositioned(it.positionInRoot())
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Transparent,
                            maxLines = 1,
                        )
                    }
                        }
                    } else {
                Column(
                    modifier = Modifier
                        .heightIn(max = 560.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused && searchQuery.isNotBlank()) {
                                        resultsExpanded = true
                                    }
                                },
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                resultsExpanded = it.isNotBlank()
                            },
                            label = {
                                Text(
                                    "搜索城市或区县",
                                    modifier = Modifier.graphicsLayer { alpha = expandedContentAlpha },
                                )
                            },
                            singleLine = true,
                            shape = controlShape,
                        )
                        IconButton(
                            modifier = Modifier.size(64.dp).offset(y = 5.dp),
                            enabled = searchQuery.isNotBlank() && !isCitySearching,
                            onClick = {
                                val keyword = searchQuery.trim()
                                if (keyword.isNotBlank()) {
                                    if (hapticFeedbackEnabled) {
                                        context.performAppVibration(AppVibration.StrongImpact)
                                    }
                                    resultsExpanded = true
                                    lastSubmittedQuery = keyword
                                    onCitySearch(keyword)
                                }
                            },
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = if (isCitySearching) "搜索中" else "搜索",
                                modifier = Modifier.size(32.dp),
                                tint = if (searchQuery.isNotBlank() && !isCitySearching) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                                },
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = resultsExpanded &&
                            searchQuery.isNotBlank() &&
                            (!citySearchMessage.isNullOrBlank() || visibleResults.isNotEmpty())
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (!citySearchMessage.isNullOrBlank()) {
                                Text(
                                    text = citySearchMessage.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            visibleResults.forEach { region ->
                                TextButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(controlShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.42f)),
                                    shape = controlShape,
                                    onClick = {
                                        if (hapticFeedbackEnabled) {
                                            context.performAppVibration(AppVibration.StrongImpact)
                                        }
                                        skipNextAutoSearch = true
                                        searchQuery = ""
                                        lastSubmittedQuery = ""
                                        resultsExpanded = false
                                        onExpandedChange(false)
                                        focusManager.clearFocus()
                                        onRegionSelected(region)
                                    },
                                ) {
                                    Icon(Icons.Filled.LocationOn, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.Start,
                                    ) {
                                        Text(
                                            text = region.shortName,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Text(
                                            text = region.displayName,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        text = "当前：${selectedRegion.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = ::locateCurrentCity) {
                            Icon(Icons.Filled.MyLocation, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("定位当前")
                        }
                        TextButton(
                            onClick = {
                                onExpandedChange(false)
                                resultsExpanded = false
                                focusManager.clearFocus()
                            },
                        ) {
                            Text("收起")
                        }
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        LocationMethod.entries.forEach { method ->
                            SelectableChip(
                                label = method.label,
                                icon = method.icon,
                                selected = locationMethod == method,
                                onClick = {
                                    if (hapticFeedbackEnabled) {
                                        context.performAppVibration(AppVibration.StrongImpact)
                                    }
                                    onLocationMethodChanged(method)
                                },
                            )
                        }
                    }
                    AnimatedVisibility(visible = !locationMessage.isNullOrBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(controlShape)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.62f))
                                .padding(10.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Icon(
                                Icons.Filled.MyLocation,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = locationMessage.orEmpty(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = controlShape,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.58f),
                        tonalElevation = 2.dp,
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = "已添加城市 · ${savedRegions.size}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            savedRegions.forEach { region ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable {
                                            skipNextAutoSearch = true
                                            searchQuery = ""
                                            lastSubmittedQuery = ""
                                            resultsExpanded = false
                                            onExpandedChange(false)
                                            focusManager.clearFocus()
                                            onRegionSelected(region)
                                        }
                                        .padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Filled.LocationOn,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = if (region.storageKey == selectedRegion.storageKey) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(region.locationButtonName, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            region.displayName,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                    IconButton(
                                        enabled = region.storageKey != selectedRegion.storageKey,
                                        onClick = { onSavedRegionRemoved(region) },
                                    ) {
                                        Icon(Icons.Filled.Close, contentDescription = "移除${region.locationButtonName}")
                                    }
                                }
                            }
                        }
                    }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = !expanded,
                enter = fadeIn(tween(120)),
                exit = fadeOut(tween(80)),
                modifier = Modifier.offset(x = 10.dp, y = 56.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = refreshLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = refreshLabelColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    refreshSecondaryLabel?.let { status ->
                        Text(
                            text = status,
                            style = MaterialTheme.typography.labelSmall,
                            color = refreshLabelColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun WeatherHero(
    region: District,
    snapshot: WeatherSnapshot,
    profile: UserProfile,
    currentTimeMillis: Long,
    isNight: Boolean,
    collapseProgress: Float,
    foregroundColor: Color,
    onTemperaturePositioned: (Offset) -> Unit,
) {
    val temperatureUnit = LocalTemperatureUnit.current
    val fused = snapshot.fused
    val today = snapshot.dailyForecast.firstOrNull { !it.isYesterday }
    val reminderCondition = reminderConditionForNextThreeHours(
        timeMillis = currentTimeMillis,
        hourlyForecast = snapshot.hourlyForecast,
        fallback = fused.condition,
    )
    val greeting = weatherGreetingFor(
        timeMillis = currentTimeMillis,
        region = region,
        condition = fused.condition,
        isNight = isNight,
        reminderCondition = reminderCondition,
        profile = profile,
    )
    val supportingFadeProgress = ((collapseProgress - 0.24f) / 0.58f).coerceIn(0f, 1f)
    val supportingAlpha = 1f - supportingFadeProgress
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 220.dp)
            .floatingCardMotion(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                alpha = supportingAlpha
                translationY = -supportingFadeProgress * 22f
            },
        ) {
            AnimatedWeatherGif(
                condition = fused.condition,
                isNight = isNightTime(currentTimeMillis, snapshot.astronomy, region.longitude),
                modifier = Modifier.size(118.dp),
            )
            Text(
                text = "${greeting.salutation} ${greeting.weatherLine}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = foregroundColor,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
            Text(
                text = greeting.prompt,
                modifier = Modifier.padding(top = 3.dp, start = 12.dp, end = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = foregroundColor.copy(alpha = 0.86f),
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
        Text(
            text = temperatureUnit.format(fused.temperatureC),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 92.sp),
            fontWeight = FontWeight.Bold,
            color = Color.Transparent,
            maxLines = 1,
            modifier = Modifier.onGloballyPositioned {
                onTemperaturePositioned(it.positionInRoot())
            },
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                alpha = supportingAlpha
                translationY = -supportingFadeProgress * 30f
            },
        ) {
            Text(
                text = today?.let {
                    "白天 ${temperatureUnit.format(it.highC)} · 夜间 ${temperatureUnit.format(it.lowC)}"
                } ?: "体感 ${temperatureUnit.format(fused.feelsLikeC)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = foregroundColor.copy(alpha = 0.92f),
            )
            Text(
                text = region.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = foregroundColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "体感 ${temperatureUnit.format(fused.feelsLikeC)} · 融合置信度 ${fused.confidencePercent}%",
                style = MaterialTheme.typography.bodySmall,
                color = foregroundColor.copy(alpha = 0.82f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

internal data class WeatherGreeting(
    val salutation: String,
    val weatherLine: String,
    val prompt: String,
)

internal fun weatherGreetingFor(
    timeMillis: Long,
    region: District,
    condition: WeatherCondition,
    isNight: Boolean? = null,
    reminderCondition: WeatherCondition = condition,
    profile: UserProfile,
): WeatherGreeting {
    val calendar = Calendar.getInstance(greetingTimeZone(region), Locale.CHINA).apply {
        this.timeInMillis = timeMillis
    }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    val greetingIsNight = isNight ?: (hour < 6 || hour >= 18)
    val isRegularWorkday = dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
    val salutation = when (hour) {
        in 1..4 -> "你还在啊？！（打哈欠）"
        in 5..7 -> "早安~~"
        in 8..10 -> "上午好~"
        in 11..13 -> "中午好~"
        in 14..17 -> "下午好~"
        in 18..22 -> "晚上好~"
        else -> "晚安~~"
    }
    val weatherLine = when (condition) {
        WeatherCondition.Sunny -> "现在天气不错"
        WeatherCondition.Cloudy -> if (greetingIsNight) "现在没有月亮哦" else "现在没有太阳哦"
        WeatherCondition.Fog -> "现在外面有雾"
        WeatherCondition.Haze -> "现在有霾，注意防护"
        WeatherCondition.Dust -> "现在有浮尘"
        WeatherCondition.Sandstorm -> "现在有沙尘暴，尽量别出门"
        WeatherCondition.Shower -> "现在正在下阵雨"
        WeatherCondition.HeavyShower -> "现在正在下强阵雨"
        WeatherCondition.LightRain -> "现在正在下小雨"
        WeatherCondition.ModerateRain -> "现在正在下中雨"
        WeatherCondition.Rain -> "现在正在下雨"
        WeatherCondition.HeavyRain -> "现在正在下大雨"
        WeatherCondition.Rainstorm -> "现在正在下暴雨"
        WeatherCondition.ThunderShower -> "现在有雷阵雨"
        WeatherCondition.Hail -> "现在可能有冰雹"
        WeatherCondition.Storm -> "现在天气不太安分"
        WeatherCondition.Snow -> "现在正在下雪"
        WeatherCondition.Sleet -> "现在正在下雨夹雪"
        WeatherCondition.FreezingRain -> "现在正在下冻雨"
    }
    val seed = dayOfYear * 7 + hour + reminderCondition.ordinal * 3 + profile.occupation.ordinal
    fun choose(options: List<String>): String = options[Math.floorMod(seed, options.size)]
    val prompt = when {
        hour in 0..4 || hour == 23 -> choose(
            listOf(
                "还没睡呢？",
                "明天天气怎么样？让我看看~",
                "该让眼睛休息一下啦~",
            )
        )
        hour in 5..9 -> choose(
            if (reminderCondition.isRainLike) {
                listOf(
                    "早餐吃什么呢？出门别忘了伞~",
                    "先吃点热乎的再出门吧~",
                    "新的一天，记得把雨具带好呀~",
                )
            } else {
                listOf(
                    "早餐吃什么呢？",
                    "新的一天，早上起来喝点水吧~",
                    "今天也从一顿美味的早餐开始吧~",
                )
            }
        )
        hour in 10..11 && reminderCondition == WeatherCondition.Sunny -> choose(
            listOf("出门逛逛？", "要不要出去晒晒太阳？", "天气正好，走两步吧~")
        )
        hour in 10..11 && reminderCondition == WeatherCondition.Cloudy -> choose(
            listOf("出门逛逛？", "趁天气温和出去走走？", "云多一点，也很适合散步呀~")
        )
        hour in 11..13 -> choose(
            listOf("午饭想吃点什么？", "到饭点啦，吃点喜欢的吧~", "中午也要好好补充能量呀~")
        )
        hour in 14..17 && reminderCondition == WeatherCondition.Sunny -> choose(
            listOf("忙一会儿，也记得看看窗外~", "要不要出去走走？", "下午的阳光还不错哦~")
        )
        hour in 14..17 && reminderCondition == WeatherCondition.Cloudy -> choose(
            listOf("忙累了就起来活动一下吧~", "出去走走？今天不会太晒~", "给自己留几分钟透透气吧~")
        )
        hour in 18..21 && isRegularWorkday -> choose(
            when (profile.occupation) {
                Occupation.Student -> listOf(
                    "学习一天了，犒劳一下自己吧！",
                    "今天的学习告一段落了吗？休息一下吧~",
                    "学习辛苦啦，晚一点也别忘了放松~",
                )
                Occupation.Office, Occupation.Outdoor -> listOf(
                    "工作一天了，犒劳一下自己吧！",
                    "今天忙完了吗？下班后放松一下吧~",
                    "工作辛苦啦，给今晚留点自己的时间~",
                )
                Occupation.Homebody -> listOf(
                    "今天也认真生活了一天，犒劳一下自己吧！",
                    "晚上啦，给自己安排一点喜欢的事吧~",
                    "忙了一天，舒服地歇一会儿吧~",
                )
                Occupation.Other -> listOf(
                    "忙了一天了，犒劳一下自己吧！",
                    "今天辛苦啦，晚上放松一下吧~",
                    "给今晚留一点轻松的时间吧~",
                )
            }
        )
        reminderCondition.isRainLike -> choose(
            listOf("外出记得带伞呀~", "路面可能有点滑，慢一点哦~", "听听雨声，也别忘了照顾好自己~")
        )
        hour in 18..22 -> choose(
            listOf("晚饭吃好了吗？", "今天过得怎么样？", "晚上也要留一点时间给自己~")
        )
        else -> choose(
            listOf("忙累了就休息一下吧~", "今天想做点什么？", "记得偶尔抬头看看天气呀~")
        )
    }
    return WeatherGreeting(salutation, weatherLine, prompt)
}

internal fun reminderConditionForNextThreeHours(
    timeMillis: Long,
    hourlyForecast: List<HourlyForecast>,
    fallback: WeatherCondition,
): WeatherCondition {
    val windowEnd = timeMillis + 3 * 60 * 60 * 1_000L
    val upcoming = hourlyForecast
        .asSequence()
        .filter { it.timeMillis > timeMillis && it.timeMillis <= windowEnd }
        .sortedBy(HourlyForecast::timeMillis)
        .toList()
    if (upcoming.isEmpty()) return fallback
    upcoming
        .map(HourlyForecast::condition)
        .filter(WeatherCondition::isSevereWeather)
        .maxByOrNull(WeatherCondition::intensityRank)
        ?.let { return it }
    upcoming
        .map(HourlyForecast::condition)
        .filter { it.visualFamily == WeatherVisualFamily.Thunder }
        .maxByOrNull(WeatherCondition::intensityRank)
        ?.let { return it }
    upcoming.firstOrNull { it.condition.visualFamily == WeatherVisualFamily.Snow }?.condition?.let { return it }
    upcoming
        .map(HourlyForecast::condition)
        .filter(WeatherCondition::isRainLike)
        .maxByOrNull(WeatherCondition::intensityRank)
        ?.let { return it }
    if (upcoming.any { (it.rainProbability ?: 0.0) >= 45.0 }) {
        return WeatherCondition.Rain
    }
    return upcoming
        .groupBy { it.condition }
        .maxByOrNull { (_, hours) -> hours.size }
        ?.key
        ?: fallback
}

private fun greetingTimeZone(region: District): TimeZone = when (region.countryCode) {
    "CN" -> TimeZone.getTimeZone("Asia/Shanghai")
    "US" -> when {
        region.province.contains("Hawaii", ignoreCase = true) -> TimeZone.getTimeZone("Pacific/Honolulu")
        region.province.contains("Alaska", ignoreCase = true) -> TimeZone.getTimeZone("America/Anchorage")
        region.longitude < -114.0 -> TimeZone.getTimeZone("America/Los_Angeles")
        region.longitude < -101.0 -> TimeZone.getTimeZone("America/Denver")
        region.longitude < -86.0 -> TimeZone.getTimeZone("America/Chicago")
        else -> TimeZone.getTimeZone("America/New_York")
    }
    else -> {
        val offsetMinutes = (region.longitude / 15.0 * 60.0).roundToInt()
        SimpleTimeZone(offsetMinutes * 60_000, "WeatherLocal")
    }
}

@Composable
internal fun AnimatedWeatherGif(
    condition: WeatherCondition,
    isNight: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = condition to isNight,
        transitionSpec = {
            fadeIn(tween(520, easing = FastOutSlowInEasing)) togetherWith
                fadeOut(tween(520, easing = FastOutSlowInEasing))
        },
        label = "weatherGifTransition",
    ) { (displayedCondition, displayedNight) ->
        if (displayedCondition.visualFamily in setOf(
                WeatherVisualFamily.Snow,
                WeatherVisualFamily.Atmosphere,
                WeatherVisualFamily.Dust,
            )
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                WeatherMiniIcon(displayedCondition, large = true, isNight = displayedNight)
            }
        } else {
            AnimatedWeatherGifAsset(displayedCondition, displayedNight, Modifier.fillMaxSize())
        }
    }
}

@Composable
internal fun AnimatedWeatherGifAsset(
    condition: WeatherCondition,
    isNight: Boolean,
    modifier: Modifier = Modifier,
) {
    val resourceId = when (condition.visualFamily) {
        WeatherVisualFamily.Clear -> if (isNight) R.drawable.weather_sunny_night_anim else R.drawable.weather_sunny_anim
        WeatherVisualFamily.Cloud, WeatherVisualFamily.Atmosphere, WeatherVisualFamily.Dust ->
            if (isNight) R.drawable.weather_cloudy_night_anim else R.drawable.weather_cloudy_anim
        WeatherVisualFamily.Rain, WeatherVisualFamily.Snow ->
            if (isNight) R.drawable.weather_rain_night_anim else R.drawable.weather_rain_anim
        WeatherVisualFamily.Thunder ->
            if (isNight) R.drawable.weather_storm_night_anim else R.drawable.weather_storm_anim
    }
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            }
        },
        update = { imageView ->
            if (imageView.tag != resourceId) {
                (imageView.drawable as? Animatable)?.stop()
                val drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeDrawable(
                        ImageDecoder.createSource(imageView.resources, resourceId)
                    )
                } else {
                    imageView.resources.getDrawable(resourceId, imageView.context.theme)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    (drawable as? AnimatedImageDrawable)?.repeatCount = AnimatedImageDrawable.REPEAT_INFINITE
                }
                imageView.setImageDrawable(drawable)
                imageView.tag = resourceId
                (drawable as? Animatable)?.start()
            }
        },
    )
}

